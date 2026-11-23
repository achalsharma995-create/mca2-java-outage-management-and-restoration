export interface WorkOrder {
  id: number;
  woNumber: string;
  outageId: number;
  outageNumber: string;
  outageTitle: string;
  outageSeverity: string;
  outageAreaName: string;
  crewId?: number;
  crewCode?: string;
  crewName?: string;
  crewVehicleNumber?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'EMERGENCY';
  status: 'CREATED' | 'ASSIGNED' | 'EN_ROUTE' | 'WORKING' | 'COMPLETED' | 'CANCELLED';
  assignedAt?: string;
  dispatchedAt?: string;
  arrivedAt?: string;
  completedAt?: string;
  estimatedDurationMinutes: number;
  instructions?: string;
  resolutionNotes?: string;
  createdByUsername: string;
  createdAt: string;
}

export interface WorkOrderRequest {
  outageId: number;
  crewId?: number;
  priority: string;
  estimatedDurationMinutes?: number;
  instructions?: string;
}
