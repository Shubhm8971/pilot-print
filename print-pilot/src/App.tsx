import { useState, useEffect } from 'react';
import { ViewType, Order, ShopSettings, ActivityLog } from './types';
import { INITIAL_ORDERS, INITIAL_SETTINGS, INITIAL_LOGS } from './data/initialData';
import { LoginScreen } from './components/LoginScreen';
import { Sidebar } from './components/Sidebar';
import { Header } from './components/Header';
import { DashboardView } from './components/DashboardView';
import { LiveQueueView } from './components/LiveQueueView';
import { ReadyForPickupView } from './components/ReadyForPickupView';
import { OrdersView } from './components/OrdersView';
import { PaymentsView } from './components/PaymentsView';
import { SettingsView } from './components/SettingsView';
import { RunLogView } from './components/RunLogView';
import { NewOrderModal } from './components/NewOrderModal';

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
  const [currentView, setCurrentView] = useState<ViewType>('dashboard');
  const [orders, setOrders] = useState<Order[]>(INITIAL_ORDERS);
  const [settings, setSettings] = useState<ShopSettings>(INITIAL_SETTINGS);
  const [logs, setLogs] = useState<ActivityLog[]>(INITIAL_LOGS);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [counterMode, setCounterMode] = useState<boolean>(false);
  const [isOpen, setIsOpen] = useState<boolean>(true);
  const [isNewOrderModalOpen, setIsNewOrderModalOpen] = useState<boolean>(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  useEffect(() => {
    let active = true;

    const syncApprovedOrders = async () => {
      try {
        const response = await fetch('https://print-pilot-ops.shubhmittal8971.workers.dev/shop/orders');
        if (!response.ok) return;
        const remoteOrders = (await response.json()) as Array<Partial<Order> & { id: string }>;
        if (!active) return;
        setOrders((current) => {
          const existingIds = new Set(current.map((order) => order.id));
          const incoming = remoteOrders
            .filter((order) => !existingIds.has(order.id))
            .map((order): Order => ({
              id: order.id,
              queueNumber: '#LIVE',
              customerName: order.customerName || 'Print Pilot customer',
              fileName: order.fileName || 'Print request',
              pageCount: order.pageCount || 1,
              copies: 1,
              colorMode: 'B&W',
              paperSize: 'A4',
              binding: 'None',
              amount: 0,
              paymentStatus: 'Prepaid',
              pin: '----',
              isPriority: Boolean(order.isPriority),
              isBuddyPickup: false,
              status: 'queued',
              createdAt: order.createdAt || new Date().toISOString(),
              estimatedWait: order.estimatedWait || 'Awaiting shop action',
            }));
          return incoming.length ? [...incoming, ...current] : current;
        });
      } catch {
        // The local demo queue remains usable when the service is unavailable.
      }
    };

    void syncApprovedOrders();
    const intervalId = window.setInterval(syncApprovedOrders, 15_000);
    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  }, []);

  // Keyboard shortcut listener for Cmd/Ctrl+K search focus
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        const searchInput = document.querySelector('input[type="text"]') as HTMLInputElement;
        searchInput?.focus();
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage((current) => (current === msg ? null : current));
    }, 3500);
  };

  const addLog = (orderId: string, action: string, customerName: string, type: 'info' | 'success' | 'warning' | 'print' = 'info') => {
    const newLog: ActivityLog = {
      id: `log-${Date.now()}`,
      orderId,
      action,
      customerName,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      type,
    };
    setLogs((prev) => [newLog, ...prev]);
  };

  const handleStartPrinting = (orderId: string) => {
    const target = orders.find((o) => o.id === orderId);
    if (!target) return;

    setOrders((prev) =>
      prev.map((o) =>
        o.id === orderId ? { ...o, status: 'printing' } : o
      )
    );
    addLog(orderId, 'Dispatched to Printer Engine', target.customerName, 'print');
    showToast(`Order ${orderId} dispatched to printer!`);
  };

  const handleMarkReady = (orderId: string) => {
    const target = orders.find((o) => o.id === orderId);
    if (!target) return;

    const timeStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    setOrders((prev) =>
      prev.map((o) =>
        o.id === orderId ? { ...o, status: 'ready', readyAt: timeStr } : o
      )
    );
    addLog(orderId, 'Print Finished • Placed in Pickup Tray', target.customerName, 'success');
    showToast(`Order ${orderId} marked ready for pickup!`);
  };

  const handleConfirmPickup = (orderId: string) => {
    const target = orders.find((o) => o.id === orderId);
    if (!target) return;

    const timeStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    setOrders((prev) =>
      prev.map((o) =>
        o.id === orderId ? { ...o, status: 'completed', completedAt: timeStr } : o
      )
    );
    addLog(orderId, 'Order Collected & Handover Verified', target.customerName, 'success');
    showToast(`Order ${orderId} successfully handed over!`);
  };

  const handleCreateOrder = (newOrder: Order, printImmediately: boolean) => {
    setOrders((prev) => [newOrder, ...prev]);
    setIsNewOrderModalOpen(false);
    addLog(
      newOrder.id,
      printImmediately ? 'Walk-in Print Dispatched Immediately' : 'New Order Queued at Counter',
      newOrder.customerName,
      printImmediately ? 'print' : 'info'
    );
    showToast(
      printImmediately
        ? `Order ${newOrder.id} sent directly to printer!`
        : `Order ${newOrder.id} added to live queue!`
    );
  };

  if (!isAuthenticated) {
    return <LoginScreen onLogin={() => setIsAuthenticated(true)} />;
  }

  const queueCount = orders.filter((o) => o.status === 'queued').length;
  const readyCount = orders.filter((o) => o.status === 'ready').length;

  return (
    <div className="h-full flex overflow-hidden text-white antialiased bg-[#0a0a0a]">
      {/* Side Navigation Bar */}
      <Sidebar
        currentView={currentView}
        onNavigate={(view) => {
          setCurrentView(view);
          setSearchQuery('');
        }}
        queueCount={queueCount}
        readyCount={readyCount}
        onLogout={() => setIsAuthenticated(false)}
        shopName={settings.shopName}
      />

      {/* Main Content Workspace */}
      <div className="flex-1 flex flex-col md:ml-64 w-full h-screen overflow-y-auto bg-[#0a0a0a]">
        {/* Sticky Top Bar */}
        <Header
          currentView={currentView}
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          counterMode={counterMode}
          onToggleCounterMode={() => setCounterMode(!counterMode)}
          isOpen={isOpen}
          onToggleStoreOpen={() => setIsOpen(!isOpen)}
          onOpenNewOrder={() => setIsNewOrderModalOpen(true)}
          recentLogs={logs}
        />

        {/* View Routing */}
        <main className="flex-1 pb-16">
          {currentView === 'dashboard' && (
            <DashboardView
              orders={orders}
              onMarkReady={handleMarkReady}
              onNavigateToQueue={() => setCurrentView('queue')}
              onNavigateToReady={() => setCurrentView('ready')}
              onNavigateToCompleted={() => setCurrentView('completed')}
              onNavigateToPayments={() => setCurrentView('payments')}
              counterMode={counterMode}
            />
          )}

          {currentView === 'queue' && (
            <LiveQueueView
              orders={orders}
              onStartPrinting={handleStartPrinting}
              onOpenNewOrder={() => setIsNewOrderModalOpen(true)}
              counterMode={counterMode}
              searchQuery={searchQuery}
            />
          )}

          {currentView === 'ready' && (
            <ReadyForPickupView
              orders={orders}
              onConfirmPickup={handleConfirmPickup}
              counterMode={counterMode}
              searchQuery={searchQuery}
            />
          )}

          {currentView === 'orders' && (
            <OrdersView
              orders={orders}
              onStartPrinting={handleStartPrinting}
              onMarkReady={handleMarkReady}
              onConfirmPickup={handleConfirmPickup}
              onOpenNewOrder={() => setIsNewOrderModalOpen(true)}
              searchQuery={searchQuery}
            />
          )}

          {currentView === 'completed' && (
            <OrdersView
              orders={orders.filter((o) => o.status === 'completed')}
              onStartPrinting={handleStartPrinting}
              onMarkReady={handleMarkReady}
              onConfirmPickup={handleConfirmPickup}
              onOpenNewOrder={() => setIsNewOrderModalOpen(true)}
              searchQuery={searchQuery}
            />
          )}

          {currentView === 'payments' && <PaymentsView orders={orders} />}

          {currentView === 'settings' && (
            <SettingsView
              settings={settings}
              onUpdateSettings={(newSet) => setSettings(newSet)}
            />
          )}

          {currentView === 'runlog' && <RunLogView logs={logs} />}
        </main>

        {/* Technical Micro-Telemetry Footer */}
        <footer className="h-10 bg-[#111111] border-t border-white/10 flex items-center justify-between px-6 text-[10px] uppercase tracking-[0.2em] text-white/40 shrink-0">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
            <span>Terminal: Ready</span>
          </div>
          <div className="hidden sm:block">Print Pilot Control Unit • 2026 Edition</div>
          <div className="font-mono">Protocol: 402.V.01</div>
        </footer>
      </div>

      {/* New Order Intake Modal */}
      {isNewOrderModalOpen && (
        <NewOrderModal
          settings={settings}
          onClose={() => setIsNewOrderModalOpen(false)}
          onSubmit={handleCreateOrder}
        />
      )}

      {/* Floating Action Toast Notification */}
      {toastMessage && (
        <div className="fixed bottom-14 right-6 z-50 bg-[#161616] text-white px-5 py-3.5 rounded-lg shadow-2xl flex items-center gap-3 border border-white/20 animate-in fade-in slide-in-from-bottom-5 duration-200">
          <span className="material-symbols-outlined text-white text-[20px]">
            check_circle
          </span>
          <span className="text-[13px] font-bold uppercase tracking-wider">{toastMessage}</span>
          <button
            onClick={() => setToastMessage(null)}
            className="text-white/40 hover:text-white ml-2 text-[18px] cursor-pointer"
          >
            ×
          </button>
        </div>
      )}
    </div>
  );
}
