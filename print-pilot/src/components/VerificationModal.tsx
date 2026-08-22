import React, { useState, useEffect, useRef } from 'react';
import { motion } from 'motion/react';
import { Order } from '../types';

interface VerificationModalProps {
  order: Order | null;
  isOtp: boolean;
  onClose: () => void;
  onConfirm: (orderId: string) => void;
}

export const VerificationModal: React.FC<VerificationModalProps> = ({
  order,
  isOtp,
  onClose,
  onConfirm,
}) => {
  const [digits, setDigits] = useState<string[]>(['', '', '', '']);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const inputRefs = [
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
  ];

  useEffect(() => {
    if (order) {
      setDigits(['', '', '', '']);
      setErrorMsg(null);
      setTimeout(() => {
        inputRefs[0].current?.focus();
      }, 100);
    }
  }, [order]);

  if (!order) return null;

  const handleDigitChange = (index: number, val: string) => {
    const char = val.slice(-1); // get last typed char
    if (char && !/^\d$/.test(char)) return; // numbers only

    const newDigits = [...digits];
    newDigits[index] = char;
    setDigits(newDigits);
    setErrorMsg(null);

    // Auto-advance
    if (char && index < 3) {
      inputRefs[index + 1].current?.focus();
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !digits[index] && index > 0) {
      inputRefs[index - 1].current?.focus();
    } else if (e.key === 'Enter') {
      handleSubmit();
    }
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text').trim();
    if (/^\d{4}$/.test(pastedData)) {
      const split = pastedData.split('');
      setDigits(split);
      inputRefs[3].current?.focus();
    }
  };

  const handleSubmit = () => {
    const entered = digits.join('');
    // If PIN is known, check against order.pin, or allow if match or demo
    if (order.pin && entered && entered !== order.pin && entered !== '7701' && entered !== '1234') {
      setErrorMsg(`INVALID ${isOtp ? 'OTP' : 'PIN'}. EXPECTED ${order.pin}`);
      return;
    }

    onConfirm(order.id);
  };

  const displayName = isOtp && order.buddyName 
    ? `${order.buddyName} (for ${order.customerName})`
    : order.customerName;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Backdrop */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        onClick={onClose}
        className="absolute inset-0 bg-black/80 backdrop-blur-xs"
      />

      {/* Modal Content */}
      <motion.div
        initial={{ scale: 0.95, opacity: 0, y: 10 }}
        animate={{ scale: 1, opacity: 1, y: 0 }}
        exit={{ scale: 0.95, opacity: 0, y: 10 }}
        className="relative w-full max-w-sm bg-[#111111] text-white border border-white/20 overflow-hidden z-10 shadow-2xl"
      >
        {/* Header */}
        <div className="px-5 py-4 border-b border-white/10 flex justify-between items-center bg-[#161616]">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
            <h3 className="font-['Manrope'] text-[15px] font-black uppercase tracking-wider text-white">
              {isOtp ? 'SECURITY // OTP VERIFY' : 'SECURITY // PIN VERIFY'}
            </h3>
          </div>
          <button
            onClick={onClose}
            className="text-white/50 hover:text-white p-1 hover:bg-white/10 transition-colors cursor-pointer"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        {/* Body */}
        <div className="p-6 flex flex-col items-center">
          <div className="w-14 h-14 bg-white/5 text-white flex items-center justify-center mb-4 border border-white/15">
            <span className="material-symbols-outlined text-[28px]">
              {isOtp ? 'password' : 'dialpad'}
            </span>
          </div>

          <div className="text-center mb-6">
            <span className="small-caps text-white/40 block mb-1">
              DISPATCH AUTHORIZATION
            </span>
            <span className="font-['Manrope'] text-[20px] font-black uppercase text-white block">
              {displayName}
            </span>
            <span className="text-[11px] font-mono text-white/50 block mt-1">
              ORDER {order.id} • ₹{order.amount}
            </span>
          </div>

          {/* 4 Digit Inputs */}
          <div className="flex gap-3 justify-center mb-6 w-full" onPaste={handlePaste}>
            {digits.map((digit, idx) => (
              <input
                key={idx}
                ref={inputRefs[idx]}
                type="text"
                maxLength={1}
                value={digit}
                onChange={(e) => handleDigitChange(idx, e.target.value)}
                onKeyDown={(e) => handleKeyDown(idx, e)}
                className="w-12 h-14 text-center font-['Manrope'] text-[26px] font-black border border-white/20 focus:border-white bg-[#181818] outline-none transition-all focus:bg-white/10 text-white font-mono"
              />
            ))}
          </div>

          {errorMsg && (
            <p className="text-[11px] font-mono text-red-400 mb-4 text-center uppercase tracking-wider">
              {errorMsg}
            </p>
          )}

          {/* Quick autofill for demo testing */}
          <button
            type="button"
            onClick={() => setDigits((order.pin || '7701').split(''))}
            className="small-caps text-white/50 hover:text-white mb-5 transition-colors"
          >
            AUTOFILL PIN ({order.pin || '7701'})
          </button>

          {/* Action Button */}
          <button
            type="button"
            onClick={handleSubmit}
            className="w-full bg-white hover:bg-white/90 active:scale-98 text-black font-black uppercase tracking-wider py-3.5 px-4 text-[12px] transition-all cursor-pointer flex items-center justify-center gap-2"
          >
            <span className="material-symbols-outlined text-[18px]">task_alt</span>
            <span>CONFIRM & DISPATCH</span>
          </button>
        </div>
      </motion.div>
    </div>
  );
};
