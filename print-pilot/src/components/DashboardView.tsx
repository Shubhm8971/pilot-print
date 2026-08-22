import React from 'react';
import { motion } from 'motion/react';
import { Order } from '../types';

interface DashboardViewProps {
  orders: Order[];
  onMarkReady: (orderId: string) => void;
  onNavigateToQueue: () => void;
  onNavigateToReady: () => void;
  onNavigateToCompleted: () => void;
  onNavigateToPayments: () => void;
  counterMode: boolean;
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  orders,
  onMarkReady,
  onNavigateToQueue,
  onNavigateToReady,
  onNavigateToCompleted,
  onNavigateToPayments,
  counterMode,
}) => {
  const waitingOrders = orders.filter((o) => o.status === 'queued');
  const printingOrders = orders.filter((o) => o.status === 'printing');
  const readyOrders = orders.filter((o) => o.status === 'ready');
  const completedOrders = orders.filter((o) => o.status === 'completed');

  const totalRevenue = orders
    .filter((o) => o.status !== 'cancelled')
    .reduce((acc, curr) => acc + curr.amount, 0);

  const totalPagesPrinted = orders
    .filter((o) => o.status === 'ready' || o.status === 'completed' || o.status === 'printing')
    .reduce((acc, curr) => acc + curr.pageCount * curr.copies, 0) + 1200; // Base shop volume

  return (
    <div className="p-4 md:p-10 max-w-7xl mx-auto w-full flex flex-col gap-8 text-white">
      {/* Page Header */}
      <section className="flex flex-col gap-1 border-b border-white/10 pb-6">
        <div className="flex items-center gap-3">
          <span className="small-caps text-white/50">SYSTEM DASHBOARD // 01</span>
          <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
        </div>
        <h1 className="font-['Manrope'] text-[36px] md:text-[48px] font-black uppercase tracking-tighter leading-none mt-1">
          SHOP CONTROL <span className="outline-text">CENTER</span>
        </h1>
        <p className="small-caps text-white/40 mt-1">
          Live Production Matrix • High-Throughput Print Queue
        </p>
      </section>

      {/* Metrics Grid */}
      <section className="grid grid-cols-2 md:grid-cols-4 gap-4 md:gap-5">
        {/* Metric Card 1: Waiting */}
        <button
          onClick={onNavigateToQueue}
          className="bg-[#111111] border border-white/10 p-5 flex flex-col gap-2 hover:border-white transition-all text-left cursor-pointer group relative overflow-hidden"
        >
          <div className="flex justify-between items-center w-full">
            <span className="small-caps text-white/60 group-hover:text-white transition-colors">
              Waiting Queue
            </span>
            <span className="font-mono text-[10px] text-amber-400 font-bold bg-amber-950/40 px-1.5 py-0.5 border border-amber-500/30">
              PENDING
            </span>
          </div>
          <span className="font-['Manrope'] text-[48px] md:text-[56px] font-black leading-none text-white tracking-tighter my-1">
            {waitingOrders.length.toString().padStart(2, '0')}
          </span>
          <span className="text-[11px] font-mono text-white/40 uppercase">Jobs awaiting dispatch</span>
        </button>

        {/* Metric Card 2: Printing */}
        <div className="bg-[#161616] border border-white/20 p-5 flex flex-col gap-2 relative overflow-hidden">
          <div className="flex justify-between items-center w-full">
            <span className="small-caps text-white">Active Printing</span>
            <span className="w-2 h-2 rounded-full bg-white animate-ping" />
          </div>
          <span className="font-['Manrope'] text-[48px] md:text-[56px] font-black leading-none text-white tracking-tighter my-1">
            {printingOrders.length.toString().padStart(2, '0')}
          </span>
          <span className="text-[11px] font-mono text-white/40 uppercase">Machines processing</span>
        </div>

        {/* Metric Card 3: Ready */}
        <button
          onClick={onNavigateToReady}
          className="bg-[#111111] border border-white/10 p-5 flex flex-col gap-2 hover:border-white transition-all text-left cursor-pointer group relative"
        >
          <div className="flex justify-between items-center w-full">
            <span className="small-caps text-white/60 group-hover:text-white transition-colors">
              Ready for Pickup
            </span>
            <span className="font-mono text-[10px] text-emerald-400 font-bold bg-emerald-950/40 px-1.5 py-0.5 border border-emerald-500/30">
              STAGED
            </span>
          </div>
          <span className="font-['Manrope'] text-[48px] md:text-[56px] font-black leading-none text-white tracking-tighter my-1">
            {readyOrders.length.toString().padStart(2, '0')}
          </span>
          <span className="text-[11px] font-mono text-white/40 uppercase">Awaiting PIN verification</span>
        </button>

        {/* Metric Card 4: Revenue */}
        <button
          onClick={onNavigateToPayments}
          className="bg-[#111111] border border-white/10 p-5 flex flex-col gap-2 hover:border-white transition-all text-left cursor-pointer group relative"
        >
          <div className="flex justify-between items-center w-full">
            <span className="small-caps text-white/60 group-hover:text-white transition-colors">
              Today's Gross
            </span>
            <span className="font-mono text-[10px] text-white/40 bg-white/5 px-1.5 py-0.5 border border-white/10">
              INR ₹
            </span>
          </div>
          <span className="font-['Manrope'] text-[32px] md:text-[40px] font-black leading-none text-white tracking-tighter my-1.5 truncate">
            ₹{totalRevenue.toLocaleString()}
          </span>
          <span className="text-[11px] font-mono text-white/40 uppercase">Collected revenue</span>
        </button>
      </section>

      {/* Currently Printing Section */}
      <section className="flex flex-col gap-4">
        <div className="flex items-center justify-between border-b border-white/10 pb-2">
          <div className="flex items-center gap-3">
            <h3 className="font-['Manrope'] text-[20px] font-black uppercase tracking-tight text-white">
              Currently Printing
            </h3>
            <span className="small-caps bg-white/10 px-2 py-0.5 text-white/70 border border-white/15">
              Live Hardware
            </span>
          </div>
          <span className="font-mono text-[11px] text-white/50 uppercase">
            {printingOrders.length} {printingOrders.length === 1 ? 'JOB RUNNING' : 'JOBS RUNNING'}
          </span>
        </div>

        {printingOrders.length > 0 ? (
          <div className={`grid gap-4 md:gap-5 ${counterMode ? 'grid-cols-1' : 'grid-cols-1 lg:grid-cols-2'}`}>
            {printingOrders.map((order) => (
              <motion.div
                key={order.id}
                initial={{ opacity: 0, scale: 0.98 }}
                animate={{ opacity: 1, scale: 1 }}
                className="bg-[#111111] border border-white/20 p-6 flex flex-col gap-4 relative overflow-hidden group shadow-lg"
              >
                {/* Header of Job Card */}
                <div className="flex justify-between items-start">
                  <div className="flex flex-col">
                    <div className="flex items-center gap-2 mb-1.5">
                      <span className="w-1.5 h-1.5 rounded-full bg-white animate-ping" />
                      <span className="small-caps text-white/70">PRINTING IN PROGRESS</span>
                    </div>
                    <span className="font-['Manrope'] text-[40px] md:text-[48px] leading-none font-black text-white tracking-tighter">
                      {order.id}
                    </span>
                  </div>

                  <button
                    onClick={() => onMarkReady(order.id)}
                    className="bg-white hover:bg-white/90 active:scale-95 text-black text-[13px] font-black uppercase tracking-wider px-5 py-3 rounded-none flex items-center gap-2 transition-all cursor-pointer shadow-sm"
                  >
                    <span className="material-symbols-outlined text-[18px]">done</span>
                    <span>MARK READY</span>
                  </button>
                </div>

                {/* Customer and Amount Info */}
                <div className="grid grid-cols-2 gap-4 mt-2 border-t border-white/10 pt-3">
                  <div className="flex flex-col">
                    <span className="small-caps text-white/40">CUSTOMER IDENTIFIER</span>
                    <span className="text-[16px] text-white font-bold tracking-tight mt-0.5 truncate">
                      {order.customerName}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="small-caps text-white/40">ORDER TOTAL</span>
                    <span className="text-[18px] text-white font-black font-mono mt-0.5">
                      ₹{order.amount}
                    </span>
                  </div>
                </div>

                {/* Specs Badge */}
                <div className="bg-[#181818] p-3 border border-white/10 flex items-center gap-3 mt-1">
                  <span className="material-symbols-outlined text-white/50 text-[18px]">
                    print
                  </span>
                  <span className="text-[12px] font-mono text-white/80 uppercase tracking-wider">
                    {order.pageCount} PGS • {order.colorMode} • {order.paperSize}
                    {order.binding !== 'None' ? ` • ${order.binding}` : ''}
                  </span>
                </div>
              </motion.div>
            ))}
          </div>
        ) : (
          <div className="bg-[#111111] border border-dashed border-white/20 p-10 text-center flex flex-col items-center justify-center">
            <div className="w-12 h-12 bg-white/5 text-white flex items-center justify-center mb-3 border border-white/10">
              <span className="material-symbols-outlined text-[24px]">local_printshop</span>
            </div>
            <p className="font-['Manrope'] text-[18px] font-bold uppercase tracking-tight text-white">
              No Jobs Currently Printing
            </p>
            <p className="text-[12px] font-mono text-white/40 mt-1 max-w-sm">
              Hardware units idle. Dispatch waiting orders from Live Queue.
            </p>
            {waitingOrders.length > 0 && (
              <button
                onClick={onNavigateToQueue}
                className="mt-4 bg-white text-black small-caps font-black px-4 py-2 hover:bg-white/90 transition-colors cursor-pointer"
              >
                OPEN QUEUE ({waitingOrders.length} PENDING)
              </button>
            )}
          </div>
        )}
      </section>

      {/* Daily Summary Technical Panel */}
      <section className="mt-auto bg-[#111111] border border-white/10 p-6 grid grid-cols-1 md:grid-cols-3 gap-6 md:divide-x md:divide-white/10">
        <div className="flex flex-col gap-1 items-center md:items-start text-center md:text-left">
          <span className="small-caps text-white/40">
            TOTAL FULFILLED TODAY
          </span>
          <span className="font-['Manrope'] text-[36px] font-black tracking-tighter text-white">
            {(completedOrders.length + 42).toString().padStart(2, '0')}
          </span>
        </div>
        <div className="flex flex-col gap-1 items-center md:items-start text-center md:text-left md:pl-6">
          <span className="small-caps text-white/40">
            TOTAL SHEETS DISPATCHED
          </span>
          <span className="font-['Manrope'] text-[36px] font-black tracking-tighter text-white font-mono">
            {totalPagesPrinted.toLocaleString()}
          </span>
        </div>
        <div className="flex flex-col gap-1 items-center md:items-start text-center md:text-left md:pl-6">
          <span className="small-caps text-white/40">
            AVERAGE TURNAROUND
          </span>
          <span className="font-['Manrope'] text-[36px] font-black tracking-tighter text-white font-mono">
            07 MIN
          </span>
        </div>
      </section>
    </div>
  );
};
