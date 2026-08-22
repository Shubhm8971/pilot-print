export type OrderStatus = 'queued' | 'printing' | 'ready' | 'completed' | 'cancelled';

export interface Order {
  id: string; // e.g. "PP-1042"
  queueNumber: string; // e.g. "#01"
  customerName: string;
  phone?: string;
  email?: string;
  fileName: string;
  pageCount: number;
  copies: number;
  colorMode: 'B&W' | 'Color';
  paperSize: 'A4' | 'A3' | 'Letter' | 'Legal';
  binding: 'None' | 'Spiral Bound' | 'Thesis Bind' | 'Corner Staple' | 'Hardcover';
  amount: number;
  paymentStatus: 'Prepaid' | 'Cash on Pickup' | 'Pending';
  pin: string; // 4-digit PIN
  isPriority: boolean;
  isBuddyPickup: boolean;
  buddyName?: string;
  status: OrderStatus;
  createdAt: string;
  readyAt?: string;
  completedAt?: string;
  estimatedWait: string;
}

export type ViewType = 
  | 'login'
  | 'dashboard'
  | 'queue'
  | 'orders'
  | 'ready'
  | 'completed'
  | 'payments'
  | 'settings'
  | 'runlog';

export interface ShopSettings {
  shopName: string;
  portalName: string;
  ownerName: string;
  email: string;
  isOpen: boolean;
  bwRatePerPage: number;
  colorRatePerPage: number;
  spiralBindCost: number;
  thesisBindCost: number;
  autoPrintPriority: boolean;
  soundAlerts: boolean;
}

export interface ActivityLog {
  id: string;
  orderId: string;
  action: string;
  customerName: string;
  timestamp: string;
  type: 'info' | 'success' | 'warning' | 'print';
}
