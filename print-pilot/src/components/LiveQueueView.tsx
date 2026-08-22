import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Order } from '../types';

interface LiveQueueViewProps {
  orders: Order[];
  onStartPrinting: (orderId: string) => void;
  onOpenNewOrder: () => void;
  counterMode: boolean;
  searchQuery: string;
}

export const LiveQueueView: React.FC<LiveQueueViewProps> = ({
  orders,
  onStartPrinting,
  onOpenNewOrder,
  counterMode,
  searchQuery,
}) => {
  const queuedOrders = orders
    .filter((o) => o.status === 'queued')
    .filter((o) => {
      if (!searchQuery.trim()) return true;
      const q = searchQuery.toLowerCase();
      return (
        o.id.toLowerCase().includes(q) ||
        o.customerName.toLowerCase().includes(q) ||
        o.pin.includes(q) ||
        o.fileName.toLowerCase().includes(q)
      );
    });

  return (
    <div className="p-4 md:p-10 max-w-7xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 pb-4 border-b border-white/10">
        <div>
          <div className="flex items-center gap-3">
            <span className="small-caps text-white/50">PROCESSING PIPELINE // 02</span>
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
          </div>
          <h2 className="font-['Manrope'] text-[32px] md:text-[44px] font-black uppercase tracking-tighter text-white mt-1">
            LIVE QUEUE <span className="outline-text">MATRIX</span>
          </h2>
          <p className="small-caps text-white/40 mt-1">
            Active Print Intake Queue ({queuedOrders.length.toString().padStart(2, '0')} JOBS AWAITING DISPATCH)
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={onOpenNewOrder}
            className="flex items-center gap-2 bg-white hover:bg-white/90 text-black text-[12px] font-black uppercase tracking-wider px-4 py-2.5 transition-all cursor-pointer shadow-sm active:scale-95"
          >
            <span className="material-symbols-outlined text-[16px]">add</span>
            <span>INTAKE WALK-IN</span>
          </button>

          <div className="flex items-center gap-2 text-white/70 text-[11px] font-mono bg-[#161616] px-3 py-2 border border-white/10">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
            <span className="uppercase">FEED ACTIVE</span>
          </div>
        </div>
      </div>

      {/* Queue Cards Container */}
      <div className="flex flex-col gap-4">
        <AnimatePresence>
          {queuedOrders.length > 0 ? (
            queuedOrders.map((order, idx) => {
              const isPriority = order.isPriority || idx === 0;

              return (
                <motion.div
                  key={order.id}
                  layout
                  initial={{ opacity: 0, y: 12 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, scale: 0.95 }}
                  className={`bg-[#111111] border p-6 relative overflow-hidden transition-all ${
                    isPriority
                      ? 'border-white/30 bg-gradient-to-r from-[#181818] to-[#111111]'
                      : 'border-white/10 hover:border-white/20'
                  }`}
                >
                  <div className={`grid items-center gap-6 ${counterMode ? 'grid-cols-1 md:grid-cols-3' : 'grid-cols-1 md:grid-cols-12'}`}>
                    {/* Left: ID, Priority badge & Customer (approx 3 cols) */}
                    <div className={`${counterMode ? '' : 'md:col-span-3'} flex flex-col border-b md:border-b-0 md:border-r border-white/10 pb-4 md:pb-0 md:pr-6`}>
                      <div className="flex items-center gap-2 mb-2">
                        {isPriority ? (
                          <span className="bg-red-950/60 border border-red-500/40 text-red-300 font-bold px-2 py-0.5 small-caps">
                            PRIORITY DISPATCH
                          </span>
                        ) : (
                          <span className="bg-white/5 border border-white/10 text-white/50 font-bold px-2 py-0.5 small-caps">
                            STANDARD QUEUE
                          </span>
                        )}
                      </div>

                      <div className="font-['Manrope'] text-[44px] font-black text-white tracking-tighter leading-none mb-2">
                        {order.queueNumber || `#0${idx + 1}`}
                      </div>

                      <div className="text-[13px] font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
                        <span className="material-symbols-outlined text-[16px] text-white/40">person</span>
                        <span className="truncate">{order.customerName}</span>
                      </div>

                      <div className="text-[11px] font-mono text-white/60 mt-2 flex items-center gap-2">
                        <span className="small-caps text-white/40">AUTH PIN:</span>
                        <span className="bg-[#1c1c1c] border border-white/20 px-2 py-0.5 text-white font-mono font-bold tracking-widest">
                          {order.pin}
                        </span>
                      </div>
                    </div>

                    {/* Middle: File Details & Specs (approx 6 cols) */}
                    <div className={`${counterMode ? '' : 'md:col-span-6'} flex flex-col px-0 md:px-4`}>
                      <div className="flex items-start gap-4 mb-3">
                        <div
                          className={`h-12 w-12 flex items-center justify-center shrink-0 border ${
                            isPriority
                              ? 'bg-red-950/40 border-red-500/40 text-red-300'
                              : 'bg-white/5 border-white/15 text-white'
                          }`}
                        >
                          <span className="material-symbols-outlined text-[24px]">description</span>
                        </div>

                        <div className="min-w-0 flex-1">
                          <h3 className="font-['Manrope'] text-[18px] font-bold text-white tracking-tight truncate" title={order.fileName}>
                            {order.fileName}
                          </h3>

                          <div className="flex flex-wrap items-center gap-3 mt-1.5 text-[12px] font-mono text-white/70">
                            <span className="flex items-center gap-1">
                              <span className="text-white/40">PAGES:</span>
                              <strong className="text-white">{order.pageCount}</strong>
                            </span>
                            <span>•</span>
                            <span className="flex items-center gap-1">
                              <span className="text-white/40">COPIES:</span>
                              <strong className="text-white">{order.copies}</strong>
                            </span>
                            <span>•</span>
                            <span className="flex items-center gap-1">
                              <span className="text-white/40">MODE:</span>
                              <strong className="text-white">{order.colorMode}</strong>
                            </span>
                            {order.binding !== 'None' && (
                              <>
                                <span>•</span>
                                <span className="bg-white/10 px-2 py-0.5 text-[10px] font-bold uppercase text-white border border-white/20">
                                  {order.binding}
                                </span>
                              </>
                            )}
                          </div>
                        </div>
                      </div>

                      <div className="flex items-center gap-2 text-[11px] font-mono w-fit px-3 py-1 bg-white/5 border border-white/10 text-white/60">
                        <span className="material-symbols-outlined text-[14px]">timer</span>
                        <span className="uppercase">EST WAIT: {order.estimatedWait || '1 MIN'}</span>
                      </div>
                    </div>

                    {/* Right: Action & Amount (approx 3 cols) */}
                    <div className={`${counterMode ? '' : 'md:col-span-3'} flex flex-row md:flex-col items-center md:items-end justify-between md:justify-center border-t md:border-t-0 md:border-l border-white/10 pt-4 md:pt-0 md:pl-6 gap-3`}>
                      <div className="font-['Manrope'] text-[32px] font-black text-white font-mono">
                        ₹{order.amount}
                      </div>

                      <button
                        onClick={() => onStartPrinting(order.id)}
                        className={`w-full md:w-full flex items-center justify-center gap-2 py-3 px-6 text-[12px] font-black uppercase tracking-wider transition-all cursor-pointer active:scale-95 ${
                          isPriority
                            ? 'bg-white hover:bg-white/90 text-black shadow-lg'
                            : 'bg-[#181818] border border-white/20 hover:border-white hover:bg-white/10 text-white'
                        }`}
                      >
                        <span className="material-symbols-outlined text-[18px]">print</span>
                        <span>START PRINTING</span>
                      </button>
                    </div>
                  </div>
                </motion.div>
              );
            })
          ) : (
            <div className="bg-[#111111] border border-dashed border-white/20 p-12 text-center flex flex-col items-center justify-center">
              <div className="w-14 h-14 bg-white/5 text-white flex items-center justify-center mb-3 border border-white/10">
                <span className="material-symbols-outlined text-[32px]">playlist_add_check</span>
              </div>
              <h3 className="font-['Manrope'] text-[20px] font-black uppercase tracking-tight text-white">Queue is Clear</h3>
              <p className="text-[12px] font-mono text-white/40 mt-1 max-w-sm">
                No orders are currently waiting. Walk-in customers or new web orders will appear here automatically.
              </p>
              <button
                onClick={onOpenNewOrder}
                className="mt-4 bg-white text-black small-caps font-black px-5 py-2.5 hover:bg-white/90 transition-colors cursor-pointer"
              >
                + Create Walk-in Order
              </button>
            </div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
};
