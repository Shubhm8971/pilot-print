import React, { useState } from 'react';
import { ViewType } from '../types';
import { AVATAR_URL } from '../data/initialData';

interface SidebarProps {
  currentView: ViewType;
  onNavigate: (view: ViewType) => void;
  queueCount: number;
  readyCount: number;
  onLogout: () => void;
  shopName: string;
}

export const Sidebar: React.FC<SidebarProps> = ({
  currentView,
  onNavigate,
  queueCount,
  readyCount,
  onLogout,
  shopName,
}) => {
  const [showUserMenu, setShowUserMenu] = useState(false);

  const navItems = [
    { id: 'dashboard' as ViewType, label: 'Dashboard', icon: 'dashboard', code: '01', count: 0 },
    { id: 'queue' as ViewType, label: 'Live Queue', icon: 'queue', code: '02', count: queueCount },
    { id: 'orders' as ViewType, label: 'Orders Register', icon: 'shopping_cart', code: '03', count: 0 },
    { id: 'ready' as ViewType, label: 'Ready for Pickup', icon: 'inventory_2', code: '04', count: readyCount },
    { id: 'completed' as ViewType, label: 'Completed Jobs', icon: 'check_circle', code: '05', count: 0 },
    { id: 'payments' as ViewType, label: 'Financials', icon: 'payments', code: '06', count: 0 },
    { id: 'settings' as ViewType, label: 'System Settings', icon: 'settings', code: '07', count: 0 },
    { id: 'runlog' as ViewType, label: 'Audit Run Log', icon: 'assignment', code: '08', count: 0 },
  ];

  return (
    <aside className="w-64 h-screen fixed left-0 top-0 bg-[#0a0a0a] border-r border-white/10 flex flex-col z-50 select-none text-white">
      {/* Brand Area */}
      <div className="p-5 border-b border-white/10 flex flex-col gap-1">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-7 h-7 bg-white text-black flex items-center justify-center font-black text-xs shrink-0">
              P
            </div>
            <div>
              <h1 className="font-['Manrope'] text-[18px] font-black uppercase tracking-tighter text-white leading-none">
                PRINT PILOT
              </h1>
              <p className="small-caps text-white/40 mt-1">
                {shopName || 'Shop Control'}
              </p>
            </div>
          </div>
          <div className="w-1.5 h-1.5 bg-white/40" />
        </div>
      </div>

      {/* Navigation Links */}
      <nav className="flex-1 overflow-y-auto py-4 px-3 flex flex-col gap-1.5">
        <div className="small-caps text-white/30 px-3 py-1 mb-1">Navigation Index</div>
        {navItems.map((item) => {
          const isActive = currentView === item.id;
          return (
            <button
              key={item.id}
              onClick={() => onNavigate(item.id)}
              className={`w-full flex items-center justify-between px-3 py-2.5 rounded-none text-[13px] font-bold tracking-tight uppercase transition-all cursor-pointer text-left border ${
                isActive
                  ? 'bg-white text-black border-white shadow-sm'
                  : 'bg-transparent text-white/60 border-transparent hover:bg-white/5 hover:text-white hover:border-white/10'
              }`}
            >
              <div className="flex items-center gap-3">
                <span className={`font-mono text-[10px] ${isActive ? 'text-black/50' : 'text-white/30'}`}>
                  {item.code}
                </span>
                <span className="truncate">{item.label}</span>
              </div>

              {item.count > 0 && (
                <span
                  className={`text-[10px] font-mono font-bold px-1.5 py-0.5 ${
                    isActive
                      ? 'bg-black text-white'
                      : 'bg-white/10 text-white border border-white/20'
                  }`}
                >
                  {item.count.toString().padStart(2, '0')}
                </span>
              )}
            </button>
          );
        })}
      </nav>

      {/* Bottom Technical Status & Profile */}
      <div className="p-3 border-t border-white/10 bg-[#0f0f0f] relative">
        {showUserMenu && (
          <div className="absolute bottom-full left-3 right-3 mb-2 bg-[#161616] border border-white/20 p-1.5 z-50 shadow-2xl">
            <button
              onClick={() => {
                setShowUserMenu(false);
                onNavigate('settings');
              }}
              className="w-full text-left px-3 py-2 text-[12px] font-bold uppercase tracking-wider text-white/80 hover:bg-white/10 flex items-center gap-2 cursor-pointer"
            >
              <span className="material-symbols-outlined text-[16px]">settings</span>
              Settings & Rates
            </button>
            <button
              onClick={() => {
                setShowUserMenu(false);
                onLogout();
              }}
              className="w-full text-left px-3 py-2 text-[12px] font-bold uppercase tracking-wider text-red-400 hover:bg-red-950/40 flex items-center gap-2 cursor-pointer"
            >
              <span className="material-symbols-outlined text-[16px]">logout</span>
              Terminate Session
            </button>
          </div>
        )}

        <button
          onClick={() => setShowUserMenu(!showUserMenu)}
          className="w-full flex items-center gap-3 p-2 hover:bg-white/5 border border-white/5 transition-colors cursor-pointer text-left"
        >
          <img
            src={AVATAR_URL}
            alt="Operator"
            className="w-8 h-8 rounded-none object-cover border border-white/20 grayscale"
            referrerPolicy="no-referrer"
          />
          <div className="flex-1 min-w-0">
            <p className="text-[12px] font-bold uppercase tracking-wider text-white truncate">Operator 01</p>
            <p className="text-[10px] font-mono text-white/40 truncate">admin@campusprinthub.com</p>
          </div>
          <span className="material-symbols-outlined text-white/40 text-[18px]">
            {showUserMenu ? 'expand_more' : 'unfold_more'}
          </span>
        </button>
      </div>
    </aside>
  );
};
