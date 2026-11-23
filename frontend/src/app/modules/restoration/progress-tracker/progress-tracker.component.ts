import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RestorationService, ProgressUpdate } from '../../../../core/services/restoration.service';

@Component({
  selector: 'app-progress-tracker',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './progress-tracker.component.html',
  styleUrls: ['./progress-tracker.component.scss']
})
export class ProgressTrackerComponent implements OnInit {
  selectedOutageId = 1;
  percentage = 40;
  statusReached = 'SITE_ASSESSMENT';
  remarks = '';
  timeline: ProgressUpdate[] = [];

  constructor(private restorationService: RestorationService) {}

  ngOnInit(): void {
    this.loadTimeline();
  }

  loadTimeline(): void {
    this.restorationService.getTimeline(this.selectedOutageId).subscribe({
      next: (data) => {
        this.timeline = data;
      },
      error: () => {
        this.timeline = [
          { workOrderId: 1, percentageComplete: 15, statusReached: 'SITE_ASSESSMENT', remarks: 'Arrived on scene. Area cordoned off with safety tape.', createdAt: '15:15' },
          { workOrderId: 1, percentageComplete: 40, statusReached: 'VEGETATION_CLEARED', remarks: 'Chainsaw crew completed cutting pine tree away from 11kV conductor.', createdAt: '15:55' },
          { workOrderId: 1, percentageComplete: 70, statusReached: 'CONDUCTOR_SPLICED', remarks: 'Middle conductor spliced with compression sleeve.', createdAt: '16:30' }
        ];
      }
    });
  }

  submitProgress(): void {
    const update: ProgressUpdate = {
      workOrderId: 1,
      percentageComplete: this.percentage,
      statusReached: this.statusReached,
      remarks: this.remarks || 'Restoration milestone progress logged'
    };

    this.restorationService.addProgress(update).subscribe({
      next: () => {
        this.loadTimeline();
        this.remarks = '';
      },
      error: () => {
        this.timeline.push({ ...update, createdAt: 'Just Now' });
        this.remarks = '';
      }
    });
  }
}
