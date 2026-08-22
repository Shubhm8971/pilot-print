import React from 'react';
import { ActivityLog } from '../types';

interface RunLogViewProps {
  logs: ActivityLog[];
}

export const RunLogView: React.FC<RunLogViewProps> = ({ logs }) => {
  return (
    <div className="p-4 md:p-10 max-w-5xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 pb-4 border-b border-white/10">
        <div>
          <div className="flex items-center gap-3">
            <span className="small-caps text-white/50">SYSTEM TELEMETRY // 06</span>
            <span className="w-1.5 h-1.5 rounded-full bg-white/60" />
          </div>
          <h2 className="font-['Manrope'] text-[32px] md:text-[44px] font-black uppercase tracking-tighter text-white mt-1">
            RUN <span className="outline-text">LOG</span>
          </h2>
          <p className="small-caps text-white/40 mt-1">
            Immutable Audit Trail of Machine Dispatches & Security Handshakes
          </p>
        </div>
        <span className="small-caps text-white bg-[#181818] border border-white/20 px-3 py-1.5">
          {logs.length} EVENTS LOGGED
        </span>
      </div>

      <div className="bg-[#111111] border border-white/10 overflow-hidden shadow-xl">
        <div className="divide-y divide-white/10 font-mono">
          {logs.map((log) => {
            let icon = 'info';
            let color = 'text-white bg-white/10 border-white/20';

            if (log.type === 'success') {
              icon = 'verified';
              color = 'text-emerald-400 bg-emerald-950/60 border-emerald-500/40';
            } else if (log.type === 'print') {
              icon = 'print';
              color = 'text-white bg-white/15 border-white/30';
            } else if (log.type === 'warning') {
              icon = 'warning';
              color = 'text-amber-400 bg-amber-950/60 border-amber-500/40';
            }

            return (
              <div key={log.id} className="p-4 flex items-center justify-between hover:bg-white/5 transition-colors gap-4">
                <div className="flex items-center gap-4">
                  <div className={`w-9 h-9 border flex items-center justify-center shrink-0 ${color}`}>
                    <span className="material-symbols-outlined text-[18px]">{icon}</span>
                  </div>
                  <div>
                    <p className="text-[13px] font-bold text-white uppercase tracking-tight">{log.action}</p>
                    <p className="text-[11px] text-white/50 mt-0.5">
                      ENTITY: <span className="text-white uppercase font-sans font-bold">{log.customerName}</span> • JOB: <span className="text-white font-bold">{log.orderId}</span>
                    </p>
                  </div>
                </div>
                <div className="text-[11px] font-bold text-white/40 whitespace-nowrap">
                  {log.timestamp}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
