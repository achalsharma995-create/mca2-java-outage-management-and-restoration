export interface Area {
  id: number;
  areaCode: string;
  areaName: string;
  region: string;
  city: string;
  totalCustomers: number;
}

export interface Feeder {
  id: number;
  feederCode: string;
  feederName: string;
  substationName: string;
  capacityKva: number;
  voltageLevel: string;
  status: string;
}
