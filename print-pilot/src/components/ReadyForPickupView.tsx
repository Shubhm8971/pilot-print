import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Order } from '../types';
import { VerificationModal } from './VerificationModal';

interface ReadyForPickupViewProps {
  orders: Order[];
  onConfirmPickup: (orderId: string) => void;
  counterMode: boolean;
  searchQuery: string;
}

export const ReadyForPickupView: React.FC<ReadyForPickupViewProps> = ({
  orders,
  onConfirmPickup,
  counterMode,
  searchQuery,
}) => {
  const [activeModalOrder, setActiveModalOrder] = useState<{
    order: Order;
    isOtp: boolean;
  } | null>(null);

  const readyOrders = orders
    .filter((o) => o.status === 'ready')
    .filter((o) => {
      if (!searchQuery.trim()) return true;
      const q = searchQuery.toLowerCase();
      return (
        o.id.toLowerCase().includes(q) ||
        o.customerName.toLowerCase().includes(q) ||
        o.pin.includes(q) ||
        (o.buddyName && o.buddyName.toLowerCase().includes(q))
      );
    });

  const getInitials = (name: string) => {
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };

  return (
    <div className="p-4 md:p-10 max-w-7xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Search match notice if searching */}
      {searchQuery && (
        <div className="text-[12px] font-mono text-white/70 bg-[#161616] px-4 py-2 border border-white/10">
          FILTER: <span className="font-bold text-white uppercase">"{searchQuery}"</span> ({readyOrders.length} MATCHES)
        </div>
      )}

      {/* Grid of Ready Orders */}
      <div
        className={`grid gap-6 ${
          counterMode
            ? 'grid-cols-1 md:grid-cols-2'
            : 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3'
        }`}
      >
        <AnimatePresence>
          {readyOrders.map((order) => {
            const isBuddy = order.isBuddyPickup;

            return (
              <motion.div
                key={order.id}
                layout
                initial={{ opacity: 0, scale: 0.96 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0, scale: 0.9 }}
                className="bg-[#111111] border border-white/15 overflow-hidden flex flex-col hover:border-white/40 transition-all relative shadow-lg"
              >
                {/* Buddy Badge if applicable */}
                {isBuddy && (
                  <div className="absolute top-0 right-0 bg-[#25153a] border-l border-b border-purple-500/40 text-purple-300 px-3 py-1 text-[10px] font-mono font-bold uppercase tracking-wider z-10 flex items-center gap-1.5">
                    <span className="material-symbols-outlined text-[14px]">group</span>
                    <span>BUDDY PICKUP</span>
                  </div>
                )}

                {/* Card Header */}
                <div
                  className={`p-4 border-b border-white/10 bg-[#161616] flex justify-between items-start ${
                    isBuddy ? 'pt-7' : ''
                  }`}
                >
                  <div>
                    <div className="flex items-center gap-2 mb-1">
                      <span className="bg-emerald-950/60 border border-emerald-500/40 text-emerald-300 px-2 py-0.5 small-caps">
                        READY
                      </span>
                      <span className="text-[11px] font-mono text-white/40">
                        {order.readyAt || order.createdAt}
                      </span>
                    </div>
                    <h3 className="font-['Manrope'] text-[24px] font-black text-white tracking-tighter">
                      {order.id}
                    </h3>
                  </div>

                  {!isBuddy && (
                    <div className="text-right">
                      <span className="font-['Manrope'] text-[24px] font-black text-white font-mono block">
                        ₹{order.amount}
                      </span>
                      <span className="small-caps text-white/40">
                        {order.paymentStatus}
                      </span>
                    </div>
                  )}
                </div>

                {/* Card Body */}
                <div className="p-5 flex-1 flex flex-col justify-between">
                  <div>
                    {/* Customer Row */}
                    <div className="flex items-center gap-3 mb-4">
                      <div className="relative shrink-0">
                        <div className="w-11 h-11 bg-white/10 border border-white/20 flex items-center justify-center font-['Manrope'] text-[15px] font-black text-white">
                          {getInitials(order.customerName)}
                        </div>
                        {isBuddy && (
                          <div className="absolute -bottom-1 -right-1 w-5 h-5 bg-purple-500 text-black flex items-center justify-center text-[10px] font-black">
                            B
                          </div>
                        )}
                      </div>

                      <div className="min-w-0">
                        <p className="text-[15px] font-bold uppercase tracking-tight text-white truncate">
                          {order.customerName}
                        </p>
                        {isBuddy ? (
                          <p className="text-[11px] font-mono text-purple-300 font-bold flex items-center gap-1 mt-0.5">
                            <span className="material-symbols-outlined text-[13px]">arrow_forward</span>
                            <span>PROXY: {order.buddyName || 'Friend'}</span>
                          </p>
                        ) : (
                          <p className="text-[12px] font-mono text-white/50 mt-0.5">
                            {order.pageCount} PGS • {order.colorMode} {order.paperSize}
                          </p>
                        )}
                      </div>
                    </div>

                    {isBuddy && (
                      <p className="text-[12px] font-mono text-white/50 mb-4">
                        {order.pageCount} PGS • {order.colorMode} {order.binding}
                      </p>
                    )}
                  </div>

                  {/* PIN Display or Blurred State */}
                  {isBuddy ? (
                    <div
                      className="bg-[#161616] p-3 border border-white/10 flex items-center justify-between opacity-80 cursor-not-allowed select-none"
                      title="Requires OTP for Buddy Pickup"
                    >
                      <div className="flex items-center gap-2 text-white/50">
                        <span className="material-symbols-outlined text-[16px]">pin</span>
                        <span className="small-caps text-white/70">AUTH PIN</span>
                      </div>
                      <span className="font-['Manrope'] text-[24px] font-black text-white tracking-widest blur-xs select-none font-mono">
                        XXXX
                      </span>
                    </div>
                  ) : (
                    <div className="bg-[#161616] p-3 border border-white/10 flex items-center justify-between">
                      <div className="flex items-center gap-2 text-white/50">
                        <span className="material-symbols-outlined text-[16px]">pin</span>
                        <span className="small-caps text-white/70">AUTH PIN</span>
                      </div>
                      <span className="font-['Manrope'] text-[28px] font-black text-white tracking-widest font-mono">
                        {order.pin}
                      </span>
                    </div>
                  )}
                </div>

                {/* Card Action Footer */}
                <div className="p-4 border-t border-white/10 bg-[#0f0f0f]">
                  {isBuddy ? (
                    <button
                      onClick={() => setActiveModalOrder({ order, isOtp: true })}
                      className="w-full bg-[#181818] text-white hover:bg-white hover:text-black text-[12px] font-black uppercase tracking-wider py-3 px-4 border border-white/20 hover:border-white transition-all flex justify-center items-center gap-2 cursor-pointer active:scale-98"
                    >
                      <span className="material-symbols-outlined text-[18px]">password</span>
                      <span>VERIFY OTP</span>
                    </button>
                  ) : (
                    <button
                      onClick={() => setActiveModalOrder({ order, isOtp: false })}
                      className="w-full bg-white hover:bg-white/90 text-black text-[12px] font-black uppercase tracking-wider py-3 px-4 transition-all flex justify-center items-center gap-2 shadow-sm cursor-pointer active:scale-98"
                    >
                      <span className="material-symbols-outlined text-[18px]">task_alt</span>
                      <span>CONFIRM PICKUP</span>
                    </button>
                  )}
                </div>
              </motion.div>
            );
          })}
        </AnimatePresence>

        {/* Empty State */}
        <div className="bg-[#111111] border border-dashed border-white/20 flex flex-col items-center justify-center p-8 min-h-[300px] text-center">
          <div className="w-12 h-12 bg-white/5 text-white flex items-center justify-center mb-3 border border-white/10">
            <span className="material-symbols-outlined text-[28px]">inventory_2</span>
          </div>
          <p className="text-[12px] font-mono text-white/40 max-w-[220px] uppercase tracking-wider">
            {readyOrders.length === 0
              ? 'No orders currently waiting for pickup.'
              : 'All ready orders stage here automatically.'}
          </p>
        </div>
      </div>

      {/* Verification Modal for Handover */}
      {activeModalOrder && (
        <VerificationModal
          order={activeModalOrder.order}
          isOtp={activeModalOrder.isOtp}
          onClose={() => setActiveModalOrder(null)}
          onConfirm={(orderId) => {
            onConfirmPickup(orderId);
            setActiveModalOrder(null);
          }}
        />
      )}
    </div>
  );
};
