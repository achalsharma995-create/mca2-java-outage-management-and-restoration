# UI/UX Wireframes & Screen Specifications
## Outage Management and Restoration System (OMRS) - J16

### 1. Control Room Dashboard Wireframe
```text
+-------------------------------------------------------------------------------------------------------------+
| [⚡ GridFlow OMRS]   [Role Switcher: Admin / Operator v]           [🔔 (4)] [👤 Alex Morgan (Operator)]    |
+-------------------------------------------------------------------------------------------------------------+
|                                                                                                             |
|  +--------------------+  +--------------------+  +--------------------+  +--------------------+             |
|  | ACTIVE INCIDENTS   |  | AVAILABLE CREWS    |  | RESTORED TODAY     |  | IMPACTED METERS    |             |
|  |        6           |  |      4 / 8 Ready   |  |        5           |  |       6,245        |             |
|  | 2 Critical, 3 High |  | 50% Fleet Active   |  | Avg Dur: 152 min   |  | 4 Sub-stations     |             |
|  +--------------------+  +--------------------+  +--------------------+  +--------------------+             |
|                                                                                                             |
|  [⚡ Outages & Triage]  [🚛 Crew Dispatch]  [🛠️ Progress Tracker]  [📢 Notifications & Broadcast]           |
|                                                                                                             |
|  +-- Filter Controls ------------------------------------------------------------------------------------+  |
|  | [🔍 Search ID/Area...    ] [Status: All v] [Area: All v] [Severity: All v] [+ Report New Outage]     |  |
|  +-------------------------------------------------------------------------------------------------------+  |
|                                                                                                             |
|  +-- Outage Incidents Table -----------------------------------------------------------------------------+  |
|  | Incident #    | Title & Description            | Area & Feeder          | Sev  | Status     | Actions |  |
|  |---------------+--------------------------------+------------------------+------+------------+---------|  |
|  | OUT-2026-0001 | Fallen Tree Across 11kV Line   | Green Valley (FDR-3301)| HIGH | IN_PROGRESS| [View]  |  |
|  | OUT-2026-0002 | 33kV Cable Joint Insulation    | East Bay (FDR-5502)    | CRIT | IN_PROGRESS| [View]  |  |
|  | OUT-2026-0003 | Substation Breaker Trip        | North Industrial       | HIGH | VERIFIED   | [View]  |  |
|  | OUT-2026-0004 | Pad-Mounted Transformer Smoke  | Downtown (FDR-1102)    | CRIT | REPORTED   | [View]  |  |
|  +-------------------------------------------------------------------------------------------------------+  |
+-------------------------------------------------------------------------------------------------------------+
```

### 2. Crew Dispatch & Work Order Collision Modal
```text
+---------------------------------------------------------+
| Dispatch Crew & Create Work Order                   [X] |
+---------------------------------------------------------+
| Target Outage:                                          |
| [ OUT-2026-0004 - Transformer Smoke (Market St)      v ]|
|                                                         |
| Assign Restoration Crew:                                |
| [ Alpha Rapid Response Crew (AVAILABLE)              v ]|
|   * Bravo Crew [DISPATCHED - Busy]                      |
|   * Charlie Specialist [ON_DUTY - Busy]                 |
|   * Delta Underground [DISPATCHED - Busy]               |
|                                                         |
| Priority: [ EMERGENCY  v ]   Est Duration: [ 180 min  ] |
|                                                         |
| Work Instructions & Grounding Orders:                   |
| [ Secure perimeter on Market St around smoking unit.  ] |
| [ De-energize ring main unit switchgear first.        ] |
|                                                         |
|                     [ Cancel ]  [ Dispatch Work Order ] |
+---------------------------------------------------------+
```

### 3. Field Engineer Restoration Progress Tracker
```text
+---------------------------------------------------------------------------------------------------------+
| [Post Field Progress Milestone]                   | [Chronological Outage Activity Log: OUT-2026-0001]   |
|                                                   |                                                     |
| Select Incident:                                  | (1) 14:15 - Incident Logged by Customer (0%)        |
| [ OUT-2026-0001 - Fallen Tree across 11kV Feeder] | (2) 14:35 - Control Room Verified Alarm (10%)       |
|                                                   | (3) 14:40 - Crew Bravo Dispatched (20%)             |
| Completion Slider:                                | (4) 15:15 - Site Cordoned & Tree Inspected (40%)    |
| [============----------] 70%                      | (5) 15:55 - Conductors Spliced & Pinned (70%)       |
|                                                   |                                                     |
| Milestone Reached:                                | [★] 100% Milestone will automatically mark Outage  |
| [ Conductor Spliced & Insulation Rigged         v]|     as RESTORED and free the assigned crew.         |
|                                                   |                                                     |
| Technical Remarks:                                |                                                     |
| [ Compression sleeve splice completed on B-phase. ]                                                     |
|                                                   |                                                     |
| ETR Time: [ 2026-03-10 18:30 ]                    |                                                     |
|                                                   |                                                     |
| [ Submit Milestone Update ]                       |                                                     |
+---------------------------------------------------------------------------------------------------------+
```
