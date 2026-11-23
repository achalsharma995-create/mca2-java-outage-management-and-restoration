export interface Outage {
  id: number;
  outageNumber: string;
  title: string;
  description: string;
  areaId: number;
  areaName: string;
  areaCode: string;
  feederId?: number;
  feederName?: string;
  feederCode?: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status: 'REPORTED' | 'VERIFIED' | 'IN_PROGRESS' | 'RESTORED' | 'CANCELLED';
  outageType: string;
  reportedAt: string;
  verifiedAt?: string;
  estimatedRestoreTime?: string;
  actualRestoreTime?: string;
  restorationDurationMinutes?: number;
  affectedCustomersCount: number;
  rootCause?: string;
  latitude?: number;
  longitude?: number;
  reportCount: number;
  createdByUsername?: string;
}

export interface OutageReportRequest {
  areaId: number;
  feederId?: number;
  title: string;
  description: string;
  severity: string;
  reporterName?: string;
  contactPhone?: string;
  customerId?: number;
  latitude?: number;
  longitude?: number;
}
