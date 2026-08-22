import React, { useState } from 'react';
import { ShopSettings } from '../types';

interface SettingsViewProps {
  settings: ShopSettings;
  onUpdateSettings: (newSettings: ShopSettings) => void;
}

export const SettingsView: React.FC<SettingsViewProps> = ({
  settings,
  onUpdateSettings,
}) => {
  const [formData, setFormData] = useState<ShopSettings>({ ...settings });
  const [savedSuccess, setSavedSuccess] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onUpdateSettings(formData);
    setSavedSuccess(true);
    setTimeout(() => setSavedSuccess(false), 3000);
  };

  return (
    <div className="p-4 md:p-10 max-w-5xl mx-auto w-full flex flex-col gap-6 text-white">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 pb-4 border-b border-white/10">
        <div>
          <div className="flex items-center gap-3">
            <span className="small-caps text-white/50">STATION CALIBRATION // 05</span>
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
          </div>
          <h2 className="font-['Manrope'] text-[32px] md:text-[44px] font-black uppercase tracking-tighter text-white mt-1">
            SHOP <span className="outline-text">PARAMETERS</span>
          </h2>
          <p className="small-caps text-white/40 mt-1">
            Unit Rates, Binding Costs & Dispatch Automation
          </p>
        </div>
      </div>

      {savedSuccess && (
        <div className="bg-[#111111] border border-emerald-500/50 text-emerald-300 px-4 py-3 text-[12px] font-mono uppercase tracking-wider flex items-center gap-2 shadow-lg">
          <span className="material-symbols-outlined text-[18px]">check_circle</span>
          <span>PARAMETERS SYNCHRONIZED ACROSS DISPATCH TERMINALS</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Print Pricing & Rates */}
        <div className="bg-[#111111] border border-white/10 p-6 shadow-xl">
          <h3 className="font-['Manrope'] text-[16px] font-black uppercase tracking-wider text-white mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-white/50 text-[18px]">payments</span>
            <span>PRINT & BINDING RATES (INR ₹)</span>
          </h3>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
            <div>
              <label className="block small-caps text-white/50 mb-1.5">
                B&W RATE PER PAGE (A4)
              </label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-white/40 font-mono font-bold">₹</span>
                <input
                  type="number"
                  step="0.5"
                  value={formData.bwRatePerPage}
                  onChange={(e) =>
                    setFormData({ ...formData, bwRatePerPage: parseFloat(e.target.value) || 0 })
                  }
                  className="w-full pl-8 pr-3 py-2.5 bg-[#181818] border border-white/20 text-[15px] font-mono font-bold text-white focus:border-white outline-none"
                />
              </div>
            </div>

            <div>
              <label className="block small-caps text-white/50 mb-1.5">
                COLOR RATE PER PAGE (A4)
              </label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-white/40 font-mono font-bold">₹</span>
                <input
                  type="number"
                  step="1"
                  value={formData.colorRatePerPage}
                  onChange={(e) =>
                    setFormData({ ...formData, colorRatePerPage: parseFloat(e.target.value) || 0 })
                  }
                  className="w-full pl-8 pr-3 py-2.5 bg-[#181818] border border-white/20 text-[15px] font-mono font-bold text-white focus:border-white outline-none"
                />
              </div>
            </div>

            <div>
              <label className="block small-caps text-white/50 mb-1.5">
                SPIRAL BINDING ADD-ON
              </label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-white/40 font-mono font-bold">₹</span>
                <input
                  type="number"
                  step="5"
                  value={formData.spiralBindCost}
                  onChange={(e) =>
                    setFormData({ ...formData, spiralBindCost: parseFloat(e.target.value) || 0 })
                  }
                  className="w-full pl-8 pr-3 py-2.5 bg-[#181818] border border-white/20 text-[15px] font-mono font-bold text-white focus:border-white outline-none"
                />
              </div>
            </div>

            <div>
              <label className="block small-caps text-white/50 mb-1.5">
                THESIS HARDCOVER BINDING
              </label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-white/40 font-mono font-bold">₹</span>
                <input
                  type="number"
                  step="10"
                  value={formData.thesisBindCost}
                  onChange={(e) =>
                    setFormData({ ...formData, thesisBindCost: parseFloat(e.target.value) || 0 })
                  }
                  className="w-full pl-8 pr-3 py-2.5 bg-[#181818] border border-white/20 text-[15px] font-mono font-bold text-white focus:border-white outline-none"
                />
              </div>
            </div>
          </div>
        </div>

        {/* Connected Printers & Hub */}
        <div className="bg-[#111111] border border-white/10 p-6 shadow-xl">
          <h3 className="font-['Manrope'] text-[16px] font-black uppercase tracking-wider text-white mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-white/50 text-[18px]">print</span>
            <span>CONNECTED HARDWARE UNITS</span>
          </h3>

          <div className="space-y-3 font-mono text-[12px]">
            {[
              { name: 'Canon ImageRunner Advance 2630 (B&W Main)', status: 'ONLINE', toner: '78%', tray: 'Tray 1 (A4) 500p' },
              { name: 'HP LaserJet Pro M404dn (Express B&W)', status: 'ONLINE', toner: '92%', tray: 'Tray 2 (A4) 250p' },
              { name: 'Epson EcoTank L8050 (High-Res Color)', status: 'READY', toner: 'Full Ink Tank', tray: 'Rear Feed (A4/A3)' },
            ].map((printer, i) => (
              <div key={i} className="flex flex-col sm:flex-row sm:items-center justify-between p-3.5 bg-[#181818] border border-white/10 gap-2">
                <div>
                  <div className="font-bold text-white uppercase flex items-center gap-2">
                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
                    {printer.name}
                  </div>
                  <div className="text-[11px] text-white/40 mt-1">
                    {printer.tray} • SUPPLY: {printer.toner}
                  </div>
                </div>
                <span className="small-caps bg-white/10 border border-white/20 text-white px-2.5 py-0.5 w-fit">
                  {printer.status}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Operational Preferences */}
        <div className="bg-[#111111] border border-white/10 p-6 shadow-xl">
          <h3 className="font-['Manrope'] text-[16px] font-black uppercase tracking-wider text-white mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-white/50 text-[18px]">tune</span>
            <span>AUTOMATION & DISPATCH RULES</span>
          </h3>

          <div className="space-y-4">
            <label className="flex items-center justify-between cursor-pointer">
              <div>
                <p className="text-[13px] font-bold text-white uppercase tracking-tight">Auto-Prioritize Paid Priority Queue</p>
                <p className="text-[11px] font-mono text-white/40">Route priority orders directly ahead of standard batch intake</p>
              </div>
              <input
                type="checkbox"
                checked={formData.autoPrintPriority}
                onChange={(e) => setFormData({ ...formData, autoPrintPriority: e.target.checked })}
                className="h-5 w-5 accent-white rounded-none cursor-pointer"
              />
            </label>

            <label className="flex items-center justify-between cursor-pointer border-t border-white/10 pt-3">
              <div>
                <p className="text-[13px] font-bold text-white uppercase tracking-tight">Audio Chime on Intake</p>
                <p className="text-[11px] font-mono text-white/40">Play frequency chime when counter or web order arrives</p>
              </div>
              <input
                type="checkbox"
                checked={formData.soundAlerts}
                onChange={(e) => setFormData({ ...formData, soundAlerts: e.target.checked })}
                className="h-5 w-5 accent-white rounded-none cursor-pointer"
              />
            </label>
          </div>
        </div>

        {/* Save Button */}
        <div className="flex justify-end">
          <button
            type="submit"
            className="bg-white hover:bg-white/90 text-black font-black uppercase tracking-wider text-[12px] px-8 py-3.5 shadow-lg transition-all cursor-pointer active:scale-95"
          >
            SAVE PARAMETERS
          </button>
        </div>
      </form>
    </div>
  );
};
