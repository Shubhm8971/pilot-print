import React, { useState } from 'react';
import { ViewType, ActivityLog } from '../types';

interface HeaderProps {
  currentView: ViewType;
  searchQuery: string;
  onSearchChange: (query: string) => void;
  counterMode: boolean;
  onToggleCounterMode: () => void;
  isOpen: boolean;
  onToggleStoreOpen: () => void;
  onOpenNewOrder: () => void;
  recentLogs: ActivityLog[];
}

export const Header: React.FC<HeaderProps> = ({
  currentView,
  searchQuery,
  onSearchChange,
  counterMode,
  onToggleCounterMode,
  isOpen,
  onToggleStoreOpen,
  onOpenNewOrder,
  recentLogs,
}) => {
  const [showNotifications, setShowNotifications] = useState(false);
  const [showHelp, setShowHelp] = useState(false);

  const getPageTitle = () => {
    switch (currentView) {
      case 'dashboard':
        return null;
      case 'queue':
        return null;
      case 'ready':
        return {
          title: 'READY REGISTER',
          subtitle: 'Orders waiting for verification & handover',
        };
      case 'orders':
        return {
          title: 'MASTER REGISTER',
          subtitle: 'All historical & active print dispatches',
        };
      case 'completed':
        return {
          title: 'COMPLETED ARCHIVE',
          subtitle: 'Fulfilled print jobs and handovers',
        };
      case 'payments':
        return {
          title: 'FINANCIAL AUDIT',
          subtitle: 'Cash collections, UPI and daily breakdown',
        };
      case 'settings':
        return {
          title: 'SYSTEM CONFIG',
          subtitle: 'Rates, printer hubs, and store preferences',
        };
      case 'runlog':
        return {
          title: 'AUDIT TELEMETRY',
          subtitle: 'Real-time production audit stream',
        };
      default:
        return null;
    }
  };

  const contextTitle = getPageTitle();

  return (
    <header className="sticky top-0 z-40 bg-[#0a0a0a]/95 backdrop-blur-md border-b border-white/10 px-6 py-3 h-[72px] flex items-center justify-between text-white select-none">
      {/* Left side: Context title or Synced badge */}
      <div className="flex items-center gap-3 min-w-[200px]">
        {contextTitle ? (
          <div className="flex items-center gap-3">
            <h2 className="font-['Manrope'] text-[18px] font-black uppercase tracking-tight text-white">
              {contextTitle.title}
            </h2>
            <span className="small-caps text-white/40 hidden lg:inline pl-3 border-l border-white/10">
              {contextTitle.subtitle}
            </span>
          </div>
        ) : (
          <div className="inline-flex items-center gap-2 bg-[#161616] px-3 py-1 border border-white/10">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span className="small-caps text-white/90">SYNC ACTIVE // 100%</span>
          </div>
        )}
      </div>

      {/* Center: Search Bar */}
      <div className="flex-1 max-w-md mx-6">
        <div className="relative group">
          <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-white/40 text-[18px] group-focus-within:text-white transition-colors">
            search
          </span>
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder={
              currentView === 'ready'
                ? 'SEARCH PIN, NAME, OR ORDER ID...'
                : 'SEARCH REGISTER / PIN / CUSTOMER...'
            }
            className="w-full pl-9 pr-12 py-2 bg-[#161616] focus:bg-[#1f1f1f] rounded-none border border-white/10 focus:border-white outline-none font-mono text-[13px] text-white placeholder:text-white/30 transition-all h-10 uppercase tracking-wider"
          />
          <div className="absolute right-3 top-1/2 -translate-y-1/2 hidden sm:flex items-center gap-0.5 pointer-events-none">
            <kbd className="text-[9px] font-mono bg-white/10 text-white/70 px-1.5 py-0.5 border border-white/15">
              ⌘K
            </kbd>
          </div>
        </div>
      </div>

      {/* Right Side: Actions */}
      <div className="flex items-center gap-3">
        {/* Counter Mode Switch */}
        <div className="flex items-center gap-2.5 bg-[#161616] px-3 py-1.5 border border-white/10">
          <span className="small-caps text-white/80">Counter Mode</span>
          <button
            type="button"
            role="switch"
            aria-checked={counterMode}
            onClick={onToggleCounterMode}
            className={`relative inline-flex h-4 w-8 shrink-0 cursor-pointer border border-white/20 transition-colors duration-200 ease-in-out focus:outline-none ${
              counterMode ? 'bg-white' : 'bg-black'
            }`}
          >
            <span
              className={`pointer-events-none inline-block h-3 w-3 transform transition duration-200 ease-in-out ${
                counterMode ? 'translate-x-4 bg-black' : 'translate-x-0 bg-white/50'
              }`}
            />
          </button>
        </div>

        {/* Quick Intake Button */}
        <button
          onClick={onOpenNewOrder}
          className="hidden md:flex items-center gap-2 bg-white hover:bg-white/90 text-black text-[12px] font-black uppercase tracking-wider px-3.5 py-2 transition-all cursor-pointer shadow-sm active:scale-95"
        >
          <span className="material-symbols-outlined text-[16px]">add</span>
          <span>NEW ORDER</span>
        </button>

        {/* Notification Bell */}
        <div className="relative">
          <button
            onClick={() => setShowNotifications(!showNotifications)}
            className="p-2 text-white/60 hover:text-white hover:bg-white/10 rounded-none border border-transparent hover:border-white/10 transition-colors relative cursor-pointer"
            title="Telemetry Stream"
          >
            <span className="material-symbols-outlined text-[20px]">notifications</span>
            <span className="absolute top-2 right-2 w-1.5 h-1.5 bg-white rounded-full animate-ping" />
          </button>

          {showNotifications && (
            <div className="absolute right-0 mt-2 w-80 bg-[#161616] border border-white/20 p-3 z-50 shadow-2xl">
              <div className="flex items-center justify-between pb-2 border-b border-white/10 mb-2">
                <span className="small-caps text-white">Event Log</span>
                <span className="text-[10px] font-mono text-emerald-400">● LIVE FEED</span>
              </div>
              <div className="space-y-1.5 max-h-64 overflow-y-auto">
                {recentLogs.slice(0, 5).map((log) => (
                  <div key={log.id} className="p-2 bg-[#0f0f0f] border border-white/5 text-[11px]">
                    <div className="flex items-center justify-between font-mono font-bold text-white">
                      <span>{log.orderId}</span>
                      <span className="text-[9px] text-white/40">{log.timestamp}</span>
                    </div>
                    <p className="text-white/60 mt-0.5 text-[11px]">{log.action}</p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Help Guide */}
        <div className="relative">
          <button
            onClick={() => setShowHelp(!showHelp)}
            className="p-2 text-white/60 hover:text-white hover:bg-white/10 transition-colors cursor-pointer"
            title="Terminal Guide"
          >
            <span className="material-symbols-outlined text-[20px]">help_outline</span>
          </button>

          {showHelp && (
            <div className="absolute right-0 mt-2 w-72 bg-[#161616] border border-white/20 p-4 z-50 shadow-2xl">
              <div className="small-caps text-white border-b border-white/10 pb-2 mb-3">Operator Quick-Guide</div>
              <ul className="text-[11px] font-mono text-white/70 space-y-2">
                <li>• <strong className="text-white">Live Queue:</strong> Start job to dispatch to printers.</li>
                <li>• <strong className="text-white">Dashboard:</strong> Mark Ready when output pages complete.</li>
                <li>• <strong className="text-white">Pickup:</strong> Verify customer 4-digit PIN for handover.</li>
                <li>• <strong className="text-white">Counter Mode:</strong> High-speed counter view for walk-ins.</li>
              </ul>
              <button
                onClick={() => setShowHelp(false)}
                className="mt-4 w-full py-1.5 text-center bg-white text-black small-caps font-black"
              >
                Acknowledge
              </button>
            </div>
          )}
        </div>

        {/* Store Open/Close Toggle Button */}
        <button
          onClick={onToggleStoreOpen}
          className={`flex items-center gap-2 px-3 py-1.5 border text-[11px] font-bold uppercase tracking-wider transition-all cursor-pointer ${
            isOpen
              ? 'bg-emerald-950/40 border-emerald-500/50 text-emerald-300 hover:bg-emerald-900/40'
              : 'bg-red-950/40 border-red-500/50 text-red-300 hover:bg-red-900/40'
          }`}
          title="Toggle shop operational status"
        >
          <span
            className={`w-1.5 h-1.5 rounded-full ${
              isOpen ? 'bg-emerald-400 animate-pulse' : 'bg-red-400'
            }`}
          />
          <span>{isOpen ? 'STORE OPEN' : 'STORE CLOSED'}</span>
        </button>
      </div>
    </header>
  );
};
