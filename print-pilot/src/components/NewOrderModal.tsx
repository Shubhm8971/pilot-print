import React, { useState } from 'react';
import { motion } from 'motion/react';
import { Order, ShopSettings } from '../types';

interface NewOrderModalProps {
  settings: ShopSettings;
  onClose: () => void;
  onSubmit: (order: Order, printImmediately: boolean) => void;
}

export const NewOrderModal: React.FC<NewOrderModalProps> = ({
  settings,
  onClose,
  onSubmit,
}) => {
  const [customerName, setCustomerName] = useState('');
  const [fileName, setFileName] = useState('Assignment_Document.pdf');
  const [pageCount, setPageCount] = useState(5);
  const [copies, setCopies] = useState(1);
  const [colorMode, setColorMode] = useState<'B&W' | 'Color'>('B&W');
  const [paperSize, setPaperSize] = useState<'A4' | 'A3'>('A4');
  const [binding, setBinding] = useState<'None' | 'Spiral Bound' | 'Thesis Bind' | 'Corner Staple'>('None');
  const [isPriority, setIsPriority] = useState(false);
  const [paymentStatus, setPaymentStatus] = useState<'Prepaid' | 'Cash on Pickup'>('Cash on Pickup');
  const [isBuddy, setIsBuddy] = useState(false);
  const [buddyName, setBuddyName] = useState('');

  // Calculate Amount
  const ratePerPage = colorMode === 'Color' ? settings.colorRatePerPage : settings.bwRatePerPage;
  const paperMultiplier = paperSize === 'A3' ? 2 : 1;
  const printCost = pageCount * copies * ratePerPage * paperMultiplier;
  const bindCost =
    binding === 'Spiral Bound'
      ? settings.spiralBindCost
      : binding === 'Thesis Bind'
      ? settings.thesisBindCost
      : 0;
  const priorityFee = isPriority ? 20 : 0;
  const totalAmount = Math.round(printCost + bindCost + priorityFee);

  const handleCreate = (printImmediately: boolean) => {
    if (!customerName.trim()) {
      alert('Please enter customer name');
      return;
    }

    const randomNum = Math.floor(1000 + Math.random() * 9000);
    const orderId = `PP-${randomNum}`;
    const pin = Math.floor(1000 + Math.random() * 9000).toString();

    const newOrder: Order = {
      id: orderId,
      queueNumber: `#0${Math.floor(Math.random() * 9) + 1}`,
      customerName: customerName.trim(),
      fileName: fileName.trim() || 'Document.pdf',
      pageCount,
      copies,
      colorMode,
      paperSize,
      binding,
      amount: totalAmount,
      paymentStatus,
      pin,
      isPriority,
      isBuddyPickup: isBuddy,
      buddyName: isBuddy ? buddyName.trim() || 'Friend' : undefined,
      status: printImmediately ? 'printing' : 'queued',
      createdAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      estimatedWait: isPriority ? '30 sec' : `${Math.ceil(pageCount / 5)} min`,
    };

    onSubmit(newOrder, printImmediately);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Backdrop */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        onClick={onClose}
        className="absolute inset-0 bg-black/80 backdrop-blur-sm"
      />

      {/* Modal Container */}
      <motion.div
        initial={{ scale: 0.95, opacity: 0, y: 15 }}
        animate={{ scale: 1, opacity: 1, y: 0 }}
        exit={{ scale: 0.95, opacity: 0, y: 15 }}
        className="relative w-full max-w-lg bg-[#111111] border border-white/20 overflow-hidden z-10 max-h-[90vh] flex flex-col text-white shadow-2xl"
      >
        {/* Header */}
        <div className="px-6 py-4 border-b border-white/10 flex justify-between items-center bg-[#161616]">
          <div>
            <span className="small-caps text-white/50 block">INTAKE WORKFLOW</span>
            <h3 className="font-['Manrope'] text-[18px] font-black uppercase tracking-tight text-white mt-0.5">
              NEW PRINT ORDER
            </h3>
          </div>
          <button
            onClick={onClose}
            className="text-white/50 hover:text-white p-1 hover:bg-white/10 transition-colors cursor-pointer"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        {/* Form Body */}
        <div className="p-6 overflow-y-auto space-y-4 font-mono text-[13px]">
          {/* Customer Name */}
          <div>
            <label className="block small-caps text-white/60 mb-1.5">
              CUSTOMER IDENTITY *
            </label>
            <input
              type="text"
              required
              value={customerName}
              onChange={(e) => setCustomerName(e.target.value)}
              placeholder="e.g. Rahul Sharma"
              className="w-full px-3.5 py-2.5 bg-[#181818] border border-white/20 text-white uppercase font-sans font-bold focus:border-white outline-none"
            />
          </div>

          {/* File Name */}
          <div>
            <label className="block small-caps text-white/60 mb-1.5">
              DOCUMENT FILE MANIFEST
            </label>
            <input
              type="text"
              value={fileName}
              onChange={(e) => setFileName(e.target.value)}
              placeholder="e.g. Lab_Report_Final.pdf"
              className="w-full px-3.5 py-2.5 bg-[#181818] border border-white/20 text-white font-mono text-[12px] focus:border-white outline-none"
            />
          </div>

          {/* Pages and Copies */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block small-caps text-white/60 mb-1.5">
                PAGE COUNT
              </label>
              <input
                type="number"
                min={1}
                value={pageCount}
                onChange={(e) => setPageCount(Math.max(1, parseInt(e.target.value) || 1))}
                className="w-full px-3.5 py-2.5 bg-[#181818] border border-white/20 text-white font-mono font-bold focus:border-white outline-none"
              />
            </div>
            <div>
              <label className="block small-caps text-white/60 mb-1.5">
                COPIES (SETS)
              </label>
              <input
                type="number"
                min={1}
                value={copies}
                onChange={(e) => setCopies(Math.max(1, parseInt(e.target.value) || 1))}
                className="w-full px-3.5 py-2.5 bg-[#181818] border border-white/20 text-white font-mono font-bold focus:border-white outline-none"
              />
            </div>
          </div>

          {/* Color Mode & Paper */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block small-caps text-white/60 mb-1.5">
                COLOR PROFILE
              </label>
              <select
                value={colorMode}
                onChange={(e) => setColorMode(e.target.value as any)}
                className="w-full px-3 py-2.5 bg-[#181818] border border-white/20 text-white font-mono text-[12px] focus:border-white outline-none"
              >
                <option value="B&W" className="bg-[#181818] text-white">B&W (₹{settings.bwRatePerPage}/page)</option>
                <option value="Color" className="bg-[#181818] text-white">COLOR (₹{settings.colorRatePerPage}/page)</option>
              </select>
            </div>
            <div>
              <label className="block small-caps text-white/60 mb-1.5">
                BINDING SPECS
              </label>
              <select
                value={binding}
                onChange={(e) => setBinding(e.target.value as any)}
                className="w-full px-3 py-2.5 bg-[#181818] border border-white/20 text-white font-mono text-[12px] focus:border-white outline-none"
              >
                <option value="None" className="bg-[#181818] text-white">None (Loose)</option>
                <option value="Corner Staple" className="bg-[#181818] text-white">Corner Staple (Free)</option>
                <option value="Spiral Bound" className="bg-[#181818] text-white">Spiral Bound (+₹{settings.spiralBindCost})</option>
                <option value="Thesis Bind" className="bg-[#181818] text-white">Thesis Hardcover (+₹{settings.thesisBindCost})</option>
              </select>
            </div>
          </div>

          {/* Priority & Buddy Pickup */}
          <div className="p-3.5 bg-[#161616] border border-white/10 space-y-2.5">
            <label className="flex items-center justify-between cursor-pointer">
              <span className="small-caps text-white flex items-center gap-1.5">
                <span className="material-symbols-outlined text-[16px] text-amber-400">bolt</span>
                EXPRESS PRIORITY (+₹20)
              </span>
              <input
                type="checkbox"
                checked={isPriority}
                onChange={(e) => setIsPriority(e.target.checked)}
                className="h-4 w-4 accent-white"
              />
            </label>

            <label className="flex items-center justify-between cursor-pointer pt-2 border-t border-white/10">
              <span className="small-caps text-white flex items-center gap-1.5">
                <span className="material-symbols-outlined text-[16px] text-purple-300">group</span>
                PROXY (BUDDY) PICKUP
              </span>
              <input
                type="checkbox"
                checked={isBuddy}
                onChange={(e) => setIsBuddy(e.target.checked)}
                className="h-4 w-4 accent-white"
              />
            </label>

            {isBuddy && (
              <input
                type="text"
                placeholder="Buddy Name (e.g. Aman)"
                value={buddyName}
                onChange={(e) => setBuddyName(e.target.value)}
                className="w-full mt-2 px-3 py-2 bg-[#181818] border border-white/20 text-white font-sans text-[12px] uppercase outline-none"
              />
            )}
          </div>

          {/* Summary Calculation */}
          <div className="flex justify-between items-center bg-[#181818] p-4 border border-white/10">
            <div>
              <span className="small-caps text-white/50 block">GROSS PAYABLE</span>
              <div className="font-['Manrope'] text-[28px] font-black text-white">
                ₹{totalAmount}
              </div>
            </div>

            <div className="flex items-center gap-2">
              <button
                type="button"
                onClick={() => setPaymentStatus('Prepaid')}
                className={`px-3 py-1.5 small-caps font-bold transition-all cursor-pointer ${
                  paymentStatus === 'Prepaid'
                    ? 'bg-white text-black'
                    : 'bg-[#111111] text-white/60 border border-white/20'
                }`}
              >
                UPI PREPAID
              </button>
              <button
                type="button"
                onClick={() => setPaymentStatus('Cash on Pickup')}
                className={`px-3 py-1.5 small-caps font-bold transition-all cursor-pointer ${
                  paymentStatus === 'Cash on Pickup'
                    ? 'bg-white text-black'
                    : 'bg-[#111111] text-white/60 border border-white/20'
                }`}
              >
                CASH ON PICKUP
              </button>
            </div>
          </div>
        </div>

        {/* Footer Actions */}
        <div className="p-4 border-t border-white/10 bg-[#161616] flex items-center justify-end gap-3">
          <button
            type="button"
            onClick={() => handleCreate(false)}
            className="small-caps text-white border border-white/20 hover:bg-white/10 px-4 py-2.5 font-bold transition-colors cursor-pointer"
          >
            ADD TO QUEUE
          </button>
          <button
            type="button"
            onClick={() => handleCreate(true)}
            className="bg-white hover:bg-white/90 text-black font-black uppercase tracking-wider text-[11px] px-5 py-2.5 transition-colors cursor-pointer flex items-center gap-1.5 active:scale-95"
          >
            <span className="material-symbols-outlined text-[16px]">print</span>
            <span>PRINT IMMEDIATELY</span>
          </button>
        </div>
      </motion.div>
    </div>
  );
};
