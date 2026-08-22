import React, { useState } from 'react';
import { Order, OrderStatus } from '../types';

interface OrdersViewProps {
  orders: Order[];
  onStartPrinting: (orderId: string) => void;
  onMarkReady: (orderId: string) => void;
  onConfirmPickup: (orderId: string) => void;
  onOpenNewOrder: () => void;
  searchQuery: string;
}

export const OrdersView: React.FC<OrdersViewProps> = ({
  orders,
  onStartPrinting,
  onMarkReady,
  onConfirmPickup,
  onOpenNewOrder,
  searchQuery,
}) => {
  const [statusFilter, setStatusFilter] = useState<'all' | OrderStatus>('all');

  const filteredOrders = orders.filter((o) => {
    if (statusFilter !== 'all' && o.status !== statusFilter) return false;
    if (!searchQuery.trim()) return true;
    const q = searchQuery.toLowerCase();
    return (
      o.id.toLowerCase().includes(q) ||
      o.customerName.toLowerCase().includes(q) ||
      o.pin.includes(q) ||
      o.fileName.toLowerCase().includes(q)
    );
  });

  const getStatusBadge = (status: OrderStatus) => {
    switch (status) {
      case 'queued':
        return (
          <span className="bg-amber-950/60 text-amber-300 border border-amber-500/30 px-2 py-0.5 small-caps inline-flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-amber-400" />
            QUEUED
          </span>
        );
      case 'printing':
        return (
          <span className="bg-white/10 text-white border border-white/30 px-2 py-0.5 small-caps inline-flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-white animate-ping" />
            PRINTING
          </span>
        );
      case 'ready':
        return (
          <span className="bg-emerald-950/60 text-emerald-300 border border-emerald-500/30 px-2 py-0.5 small-caps inline-flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
            READY
          </span>
        );
      case 'completed':
        return (
          <span className="bg-white/5 text-white/50 border border-white/10 px-2 py-0.5 small-caps inline-flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-white/40" />
            COMPLETED
          </span>
        );
      default:
        return null;
    }
  };

  return (
    <div className="p-4 md:p-10 max-w-7xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 pb-4 border-b border-white/10">
        <div>
          <div className="flex items-center gap-3">
            <span className="small-caps text-white/50">ORDER DIRECTORY // 03</span>
            <span className="w-1.5 h-1.5 rounded-full bg-white/60" />
          </div>
          <h2 className="font-['Manrope'] text-[32px] md:text-[44px] font-black uppercase tracking-tighter text-white mt-1">
            MASTER <span className="outline-text">REGISTRY</span>
          </h2>
          <p className="small-caps text-white/40 mt-1">
            Unified Transaction & Print Job Database
          </p>
        </div>

        <button
          onClick={onOpenNewOrder}
          className="bg-white hover:bg-white/90 text-black font-black text-[12px] uppercase tracking-wider px-5 py-3 transition-colors cursor-pointer active:scale-95 flex items-center justify-center gap-2"
        >
          <span className="material-symbols-outlined text-[18px]">add</span>
          <span>NEW PRINT ORDER</span>
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="flex flex-wrap items-center gap-2 bg-[#111111] p-1.5 border border-white/10 w-fit">
        {[
          { id: 'all', label: 'ALL ORDERS', count: orders.length },
          { id: 'queued', label: 'QUEUED', count: orders.filter((o) => o.status === 'queued').length },
          { id: 'printing', label: 'PRINTING', count: orders.filter((o) => o.status === 'printing').length },
          { id: 'ready', label: 'READY', count: orders.filter((o) => o.status === 'ready').length },
          { id: 'completed', label: 'COMPLETED', count: orders.filter((o) => o.status === 'completed').length },
        ].map((tab) => (
          <button
            key={tab.id}
            onClick={() => setStatusFilter(tab.id as any)}
            className={`px-3 py-1.5 small-caps font-bold transition-all cursor-pointer ${
              statusFilter === tab.id
                ? 'bg-white text-black'
                : 'text-white/60 hover:text-white hover:bg-white/5'
            }`}
          >
            <span>{tab.label}</span>
            <span
              className={`ml-2 text-[10px] font-mono px-1.5 py-0.5 ${
                statusFilter === tab.id
                  ? 'bg-black text-white'
                  : 'bg-white/10 text-white/70'
              }`}
            >
              {tab.count.toString().padStart(2, '0')}
            </span>
          </button>
        ))}
      </div>

      {/* Orders Table */}
      <div className="bg-[#111111] border border-white/10 overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#161616] border-b border-white/10 text-[11px] font-mono font-bold text-white/50 uppercase tracking-widest">
                <th className="py-3 px-4">IDENTIFIER</th>
                <th className="py-3 px-4">CUSTOMER</th>
                <th className="py-3 px-4">DOCUMENT SPECS</th>
                <th className="py-3 px-4">TIMESTAMP</th>
                <th className="py-3 px-4">PIN</th>
                <th className="py-3 px-4">AMOUNT</th>
                <th className="py-3 px-4">STATE</th>
                <th className="py-3 px-4 text-right">ACTION</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/10 text-[13px] font-mono">
              {filteredOrders.length > 0 ? (
                filteredOrders.map((order) => (
                  <tr key={order.id} className="hover:bg-white/5 transition-colors">
                    <td className="py-4 px-4 font-['Manrope'] font-black text-white">
                      {order.id}
                      {order.isPriority && (
                        <span className="ml-2 text-[9px] bg-red-950/60 border border-red-500/40 text-red-300 font-bold px-1.5 py-0.5 small-caps">
                          PRIORITY
                        </span>
                      )}
                    </td>
                    <td className="py-4 px-4">
                      <div className="font-bold text-white uppercase tracking-tight">{order.customerName}</div>
                      {order.buddyName && (
                        <div className="text-[11px] text-purple-300 font-mono">
                          PROXY: {order.buddyName}
                        </div>
                      )}
                    </td>
                    <td className="py-4 px-4">
                      <div className="font-bold text-white truncate max-w-[200px]" title={order.fileName}>
                        {order.fileName}
                      </div>
                      <div className="text-[11px] text-white/40">
                        {order.pageCount} PGS • {order.copies} COP • {order.colorMode}
                        {order.binding !== 'None' ? ` • ${order.binding}` : ''}
                      </div>
                    </td>
                    <td className="py-4 px-4 text-white/50 text-[12px]">
                      {order.createdAt}
                    </td>
                    <td className="py-4 px-4">
                      <span className="font-mono font-bold bg-[#181818] text-white px-2 py-0.5 border border-white/20">
                        {order.isBuddyPickup ? 'OTP' : order.pin}
                      </span>
                    </td>
                    <td className="py-4 px-4">
                      <div className="font-black text-white font-['Manrope']">₹{order.amount}</div>
                      <div className="text-[10px] small-caps text-white/40">{order.paymentStatus}</div>
                    </td>
                    <td className="py-4 px-4">
                      {getStatusBadge(order.status)}
                    </td>
                    <td className="py-4 px-4 text-right">
                      {order.status === 'queued' && (
                        <button
                          onClick={() => onStartPrinting(order.id)}
                          className="bg-white text-black hover:bg-white/90 text-[11px] font-black uppercase tracking-wider px-3 py-1.5 transition-colors cursor-pointer"
                        >
                          PRINT
                        </button>
                      )}
                      {order.status === 'printing' && (
                        <button
                          onClick={() => onMarkReady(order.id)}
                          className="bg-emerald-400 text-black hover:bg-emerald-300 text-[11px] font-black uppercase tracking-wider px-3 py-1.5 transition-colors cursor-pointer"
                        >
                          READY
                        </button>
                      )}
                      {order.status === 'ready' && (
                        <button
                          onClick={() => onConfirmPickup(order.id)}
                          className="bg-white text-black hover:bg-white/90 text-[11px] font-black uppercase tracking-wider px-3 py-1.5 transition-colors cursor-pointer"
                        >
                          HANDOVER
                        </button>
                      )}
                      {order.status === 'completed' && (
                        <span className="small-caps text-white/40 flex items-center justify-end gap-1">
                          <span className="material-symbols-outlined text-[14px]">done_all</span>
                          FULFILLED
                        </span>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-white/40 font-mono text-[12px] uppercase">
                    No records matching filter parameters.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
