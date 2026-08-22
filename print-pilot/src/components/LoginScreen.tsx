import React, { useState } from 'react';
import { motion } from 'motion/react';

interface LoginScreenProps {
  onLogin: (email: string) => void;
}

export const LoginScreen: React.FC<LoginScreenProps> = ({ onLogin }) => {
  const [identifier, setIdentifier] = useState('operator@printpilot.shop');
  const [password, setPassword] = useState('admin');
  const [rememberMe, setRememberMe] = useState(true);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setTimeout(() => {
      onLogin(identifier);
    }, 400);
  };

  const fillDemo = () => {
    setIdentifier('admin@campusprinthub.com');
    setPassword('admin');
  };

  return (
    <div className="bg-[#0a0a0a] min-h-screen flex items-center justify-center font-sans text-white antialiased p-4 md:p-10 relative overflow-hidden">
      {/* Background Grid & Architectural Lines */}
      <div className="fixed inset-0 pointer-events-none opacity-20 bg-[radial-gradient(#ffffff_1px,transparent_1px)] [background-size:24px_24px]" />
      <div className="fixed inset-0 pointer-events-none flex justify-between px-10 border-x border-white/5" />

      {/* Decorative vertical badges */}
      <div className="hidden lg:block fixed left-8 top-1/2 -translate-y-1/2 small-caps v-text text-white/30 select-none">
        System 402.V.01 // Terminal Access
      </div>
      <div className="hidden lg:block fixed right-8 top-1/2 -translate-y-1/2 small-caps v-text text-white/30 select-none">
        Edition 01/26 // Print Pilot
      </div>

      {/* Login Container */}
      <motion.div 
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.35, ease: 'easeOut' }}
        className="w-full max-w-[460px] z-10 relative"
      >
        {/* Header Section */}
        <div className="mb-8">
          <div className="flex items-center justify-between border-b border-white/10 pb-3 mb-6">
            <span className="small-caps text-white/50">Terminal.01 / Auth</span>
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
              <span className="small-caps text-white/70">Online</span>
            </div>
          </div>

          <div className="huge-type uppercase tracking-tighter">
            PRINT<br />
            <span className="outline-text">PILOT</span>
          </div>
          <p className="small-caps text-white/50 mt-2">
            Shop Control Center // Access Portal
          </p>
        </div>

        {/* Login Card */}
        <div className="bg-[#111111] border border-white/10 rounded-none p-8 shadow-2xl relative">
          <div className="absolute -top-[1px] -left-[1px] w-2 h-2 bg-white" />
          <div className="absolute -top-[1px] -right-[1px] w-2 h-2 bg-white" />
          <div className="absolute -bottom-[1px] -left-[1px] w-2 h-2 bg-white" />
          <div className="absolute -bottom-[1px] -right-[1px] w-2 h-2 bg-white" />

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Phone or Email Field */}
            <div className="space-y-2">
              <label className="block small-caps text-white/70" htmlFor="identifier">
                Operator Identifier
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-white/40">
                  <span className="material-symbols-outlined text-[18px]">person</span>
                </div>
                <input
                  id="identifier"
                  name="identifier"
                  type="text"
                  required
                  value={identifier}
                  onChange={(e) => setIdentifier(e.target.value)}
                  placeholder="operator@printpilot.shop"
                  className="block w-full pl-10 pr-3 py-3 bg-[#161616] border border-white/10 rounded-none text-white font-mono text-[14px] focus:border-white focus:ring-1 focus:ring-white outline-none transition-all placeholder:text-white/20"
                />
              </div>
            </div>

            {/* Password Field */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <label className="block small-caps text-white/70" htmlFor="password">
                  Security Passkey
                </label>
                <button
                  type="button"
                  onClick={() => alert('Demo mode: Please use password "admin" to log in.')}
                  className="small-caps text-white/40 hover:text-white transition-colors"
                >
                  Forgot?
                </button>
              </div>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-white/40">
                  <span className="material-symbols-outlined text-[18px]">lock</span>
                </div>
                <input
                  id="password"
                  name="password"
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="block w-full pl-10 pr-3 py-3 bg-[#161616] border border-white/10 rounded-none text-white font-mono text-[14px] focus:border-white focus:ring-1 focus:ring-white outline-none transition-all placeholder:text-white/20"
                />
              </div>
            </div>

            {/* Remember Me & Actions */}
            <div className="flex items-center justify-between pt-1">
              <label className="flex items-center cursor-pointer select-none">
                <input
                  type="checkbox"
                  id="remember-me"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="h-4 w-4 rounded-none bg-[#161616] border-white/20 text-white focus:ring-0 accent-white"
                />
                <span className="ml-2.5 small-caps text-white/60">
                  Persist Session
                </span>
              </label>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading}
              className="w-full flex justify-center items-center py-3.5 px-4 bg-white hover:bg-white/90 text-black font-black uppercase text-[13px] tracking-widest transition-all active:scale-[0.99] cursor-pointer disabled:opacity-50"
            >
              {isLoading ? (
                <div className="flex items-center gap-2">
                  <span className="material-symbols-outlined animate-spin text-[18px]">progress_activity</span>
                  <span>INITIALIZING...</span>
                </div>
              ) : (
                'INITIALIZE CONTROL CENTER'
              )}
            </button>
          </form>
        </div>

        {/* Demo Context Note */}
        <div className="mt-6 text-center">
          <button
            type="button"
            onClick={fillDemo}
            className="inline-flex items-center justify-center gap-2 bg-[#111] hover:bg-white/10 px-4 py-2 border border-white/10 transition-colors cursor-pointer"
          >
            <span className="small-caps text-white/40">Demo Bypass:</span>
            <span className="font-mono text-[11px] text-white font-bold tracking-wider">admin / admin</span>
          </button>
        </div>
      </motion.div>
    </div>
  );
};
