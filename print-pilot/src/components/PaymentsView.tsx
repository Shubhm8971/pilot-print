import React from 'react';
import { Order } from '../types';

interface PaymentsViewProps {
  orders: Order[];
}

export const PaymentsView: React.FC<PaymentsViewProps> = ({ orders }) => {
  const activeOrders = orders.filter((o) => o.status !== 'cancelled');

  const totalGross = activeOrders.reduce((sum, o) => sum + o.amount, 0);
  const prepaidTotal = activeOrders
    .filter((o) => o.paymentStatus === 'Prepaid')
    .reduce((sum, o) => sum + o.amount, 0);
  const cashTotal = activeOrders
    .filter((o) => o.paymentStatus === 'Cash on Pickup')
    .reduce((sum, o) => sum + o.amount, 0);
  const avgOrderVal = activeOrders.length > 0 ? Math.round(totalGross / activeOrders.length) : 0;

  return (
    <div className="p-4 md:p-10 max-w-7xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 pb-4 border-b border-white/10">
        <div>
          <div className="flex items-center gap-3">
            <span className="small-caps text-white/50">FINANCIAL AUDIT // 04</span>
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
          </div>
          <h2 className="font-['Manrope'] text-[32px] md:text-[44px] font-black uppercase tracking-tighter text-white mt-1">
            SETTLEMENT <span className="outline-text">LEDGER</span>
          </h2>
          <p className="small-caps text-white/40 mt-1">
            Real-Time Revenue Aggregation & Counter Receipts
          </p>
        </div>
      </div>

      {/* Overview Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-[#111111] p-5 border border-white/10 flex flex-col justify-between">
          <span className="small-caps text-white/50">
            TOTAL REVENUE TODAY
          </span>
          <div className="font-['Manrope'] text-[36px] font-black text-white my-2 font-mono">
            ₹{totalGross.toLocaleString()}
          </div>
          <span className="text-[11px] font-mono text-emerald-400 font-bold uppercase tracking-wider inline-flex items-center gap-1">
            <span className="material-symbols-outlined text-[14px]">trending_up</span>
            +18% VS CYCLE PRIOR
          </span>
        </div>

        <div className="bg-[#111111] p-5 border border-white/10 flex flex-col justify-between">
          <span className="small-caps text-white/50">
            DIGITAL PREPAID (UPI/CARD)
          </span>
          <div className="font-['Manrope'] text-[36px] font-black text-white my-2 font-mono">
            ₹{prepaidTotal.toLocaleString()}
          </div>
          <span className="text-[11px] font-mono text-white/40 uppercase">
            {Math.round((prepaidTotal / (totalGross || 1)) * 100)}% VOLUME SHARE
          </span>
        </div>

        <div className="bg-[#111111] p-5 border border-white/10 flex flex-col justify-between">
          <span className="small-caps text-white/50">
            COUNTER CASH DUE
          </span>
          <div className="font-['Manrope'] text-[36px] font-black text-amber-400 my-2 font-mono">
            ₹{cashTotal.toLocaleString()}
          </div>
          <span className="text-[11px] font-mono text-white/40 uppercase">
            PAYMENT ON HANDOVER
          </span>
        </div>

        <div className="bg-[#111111] p-5 border border-white/10 flex flex-col justify-between">
          <span className="small-caps text-white/50">
            AVERAGE TICKET
          </span>
          <div className="font-['Manrope'] text-[36px] font-black text-white my-2 font-mono">
            ₹{avgOrderVal}
          </div>
          <span className="text-[11px] font-mono text-white/40 uppercase">
            ACROSS {activeOrders.length} ORDERS
          </span>
        </div>
      </div>

      {/* Transactions Table */}
      <div className="bg-[#111111] border border-white/10 overflow-hidden shadow-xl">
        <div className="p-4 border-b border-white/10 flex justify-between items-center bg-[#161616]">
          <h3 className="font-['Manrope'] text-[16px] font-black uppercase tracking-wider text-white">
            TRANSACTION LOG ENTRIES
          </h3>
          <button
            onClick={() => {}}
            className="small-caps text-black bg-white hover:bg-white/90 px-3 py-1.5 font-black transition-colors cursor-pointer"
          >
            EXPORT CSV
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse font-mono text-[13px]">
            <thead>
              <tr className="bg-[#181818] border-b border-white/10 text-[11px] font-bold text-white/50 uppercase tracking-widest">
                <th className="py-3 px-4">IDENTIFIER</th>
                <th className="py-3 px-4">CUSTOMER</th>
                <th className="py-3 px-4">JOB TYPE</th>
                <th className="py-3 px-4">CHANNEL</th>
                <th className="py-3 px-4">AMOUNT</th>
                <th className="py-3 px-4 text-right">AUDIT STATE</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/10">
              {activeOrders.map((order) => (
                <tr key={order.id} className="hover:bg-white/5 transition-colors">
                  <td className="py-3.5 px-4 font-bold text-white">{order.id}</td>
                  <td className="py-3.5 px-4 text-white uppercase font-sans font-bold">{order.customerName}</td>
                  <td className="py-3.5 px-4 text-white/50 text-[12px]">
                    {order.pageCount}P {order.colorMode}
                  </td>
                  <td className="py-3.5 px-4">
                    <span
                      className={`text-[10px] font-mono font-bold px-2 py-0.5 border ${
                        order.paymentStatus === 'Prepaid'
                          ? 'bg-white/10 text-white border-white/20'
                          : 'bg-amber-950/60 text-amber-300 border-amber-500/30'
                      }`}
                    >
                      {order.paymentStatus.toUpperCase()}
                    </span>
                  </td>
                  <td className="py-3.5 px-4 font-black font-['Manrope'] text-white">₹{order.amount}</td>
                  <td className="py-3.5 px-4 text-right">
                    <span className="small-caps text-emerald-400">SETTLED</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
