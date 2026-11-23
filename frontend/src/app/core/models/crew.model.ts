export interface CrewMember {
  id?: number;
  userId?: number;
  memberName: string;
  roleDesignation: string;
  phone: string;
}

export interface Crew {
  id: number;
  crewCode: string;
  crewName: string;
  leadEngineerId?: number;
  leadEngineerName?: string;
  vehicleNumber: string;
  vehicleType: string;
  contactPhone: string;
  availabilityStatus: 'AVAILABLE' | 'DISPATCHED' | 'ON_DUTY' | 'OFF_DUTY' | 'MAINTENANCE';
  baseAreaId: number;
  baseAreaName: string;
  membersCount: number;
  skillSpecialization?: string;
  members?: CrewMember[];
  createdAt?: string;
}
