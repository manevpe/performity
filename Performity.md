Microservices:
+ FE - React (TS) / Material-UI / Redux?
+ API Gateway / Auth - Java & Spring Boot & Spring Security & Spring Cloud
+ Useradmin Service - Java & Spring Boot
- Vacation Planner / PTO Service - Java & Spring Boot
- Reporting Service - Python?
- Task Scheduler - Java Quartz
- Notification (Email) Service? - Amazon SES?
- Question / Suggestion Board
- Employee Satisfaction Surveys
- Performance Review
- Employee Career Path
- Employee Recognition
- 1-2-1 Meetings
- Org Chart
- Employee Profiles / Personalities
- Recruitment
- Employee engagement
- Invoicing & Payments - invoiceplane / invoiceninja
- Onboarding
- Org Maturity
- Auditor (for useradmin)


3rd Parties:
+ DB - Postgres
- ESB - Kafka
- Logging - Java Logger? / ELK
- Spring Boot Actuator - runtime log level change
+ Service Discovery & Config - Consul


Others:
+ OpenAPI & Swagger
+ Auth & Authorization - Keycloak
+ Unit Tests - JUnit5, Mockito, JaCoCo, PyTest?
- API / Contract Tests - TBD?
- E2E Tests - Playwright (TS)
+ Docker & Docker Compose
- k8s + Prometheus / nomad?
+ Gradle
+ JWT
- TLS
- i18n & l10n
- Backstage / Port
- new microservice template 
- My Tech Stack
- Tech Writer
- Terraform / Grafana
- Promotion and sales website
+ Checkstyle, SonarLint, SpotBugs
- GitHub CI / CD Integrations - Build Status, Unit Tests and Coverage, Code Quality - https://sonarcloud.io/


API:
- Sorting
- Pagination
- BE - FE sync on validation with JSON schema
+ Correlation IDs - Sleuth & Zipkin



Product Features:

- Org Settings: (Org Admin role)
  - Vacation Planner:
    - vacation days per year - unlimited? 365?
    - allow negative vacation days
    - allow half days
    - increment interval - quarter; semi-annually; annually
    - notification reminders - off; daily; every 3 days; weekly
    - vacation types - paid, unpaid, medical, other; CRUD?
    - vacation days carry over? reminders for losing days
    - Non-working days
  - Locations - CRUD - default is Remote; do not allow to delete in-use ones or Remote.
  - Position levels:
    - multiple verticals
    - description for each level
    - requirements for each level
    - use predefined templates
    - salary band
  - Features - turn on / off whole features
  - Company values
  - Recognition points per month; Increment - month; quarter; year; Per manager?


- Useradmin - CRUD users
  - short bio / about me
  - interests
  - important dates - hire date, birthday (Manager only) (Employee - choose visibility)
  - important events - promotions (Manager only)
  - awards (Manager only)
  - personality test
  - names (Manager only)
  - email (Manager only)
  - teams (Manager only)
  - manager (Manager only)
  - roles (Manager only)
  - position (Manager only)
  - desired next level
  - score for each requirement for current / next level (Manager only)
  - location (Manageronly)
  - vacation days (Manager only)
  - Recognitions received / given (Manager only)
  - Recognition points to give (Manager only)
  - Recognition points received - all time; this year, this quarter; this month; last * (Manager only)
  - Status - active employee or archived


- Teams:
  - CRUD teams - do not allow to delete a team with users in it
  - see users in a team
  - Team Lead


- Vacation Planner:
  - request vacation
  - manager - approve / deny requests. See other people on the same team, who are off on the same day.
  - calendar - all; my team; mine;
  - balance
  - request log
  - reporting / export table


- On-call / Shift planner:
  - TODO


- Question / Suggestion Board:
  - Create suggestion / question - choose type; choose anonymous or not
  - List all suggestions / questions - filter by status resolved; sort by votes / created / updated
  - Comment
  - Vote - positive / negative
  - Manager / Owner (non-anonimous) - mark as resolved


- Employee Recognition:
  - New recognition:
    - person / people / team
    - description
    - company value demonstrated
    - GIF?
    - public or private
  - Recognition board
  - Comment
  - Like
  - Add recognition points
  - Preview Leader board
  - Employee of the month - nominations, votes, awards


- Employee Satisfaction Surveys:
  - Manager - create a survey - add questions (free text / rating 1-5 / choice); clone previous survey
  - Survey can be saved, without sending to employees
  - List surveys - only managers can see all surveys; employees see surveys they received, even past ones
  - Delete survey
  - Edit survey - only unsent ones. Sent surveys can only be cloned.
  - Select individuals or teams or everyone to send the survey. Send the survey to employees.
  - Assign deadline
  - Set annonimous or not
  - Email reminders on the last day before the deadline
  - Manager - see report on a survey - in-progress or complete:
    - see who completed it - for annonimous see only participation %.
    - for rating / choice questions see pie charts
    - for text questions see all answers
    - for repeated surveys, compare to previous times - progress chart


- Employee Career Path:
  - Preview Org Chart
  - Preview Career path chart
  - Preview match for desired next level - endorsement is by Manager


- Performance Review:
  - Choose plugins:
    - Spider chart
    - 360 review
    - OKRs
    - Free text
    - Self-review
    - Total score
  - Create on-going review
  - Update self-review
  - Update review by Manager
  - Manager - request 360 feedback
  - Complete review
  - Set team OKRs (Manager)
  - Email Reminders to fill-in updates - bi-weekly; monthly; quarterly;
  - Compare skills to next level
  - Manager - suggest role promotion
  - Manager - suggest salary increase

 
- 1-2-1 Meetings:
  - Manager:
    - Prepare meeting notes / topics:
      - Allow to manage templates - CRUD per manager. Allow to set "default / always add" questions / topics.
      - Create schedule for meetings, get reminders to prepare
      - sync with calendar - Google / MS Office 365
      - See upcoming meetings
      - Create new meeting preparation
      - see recently used for the current person - sorted by last used in reverse order
      - Allow to add meeting notes during or after the 1-2-1 meeting


- Employee Profiles / Personalities:
  - Take a personality quiz and see results
  - See other people like you
  - See suggestions on how to best work with other profiles


- Recruitment: (Role Manager + Custom added people, per candidate)
  - Recruitment pipeline view - kanban board; Filter by job opening;
  - Analytics by job opening
  - Allow to edit the pipeline statuses, but provide a default one
  - Manage job openings - CRUD. Allow to close and reopen a job opening.
  - Add candidates
  - Allow to see repeating candidates and previous interview processes - match on Email / Phone / LinkedIn
  - Move candidates in the pipeline
  - Add comments on every stage and when moving a person in the pipeline
  - Send reminders for people, staying in the pipeline too long
  - Allow Managers to ask for feedback other people. They get a form, they need to fill in.
  - Allow to create feedback forms with free text, rating 1-5 and choice questions. Provide templates.


- Employee engagement:
  - TODO
  - See most active employees on the platform
  - See trends - sudden decline in engagement on the platform, and fire notifications


- Onboarding:
  - Create Onboarding plans - 1st week, First 30 days, First 90 days, First 180 days; per position. Allow cloning
  - Allow to assign plans to employees.
  - Employee can mark tasks as done or report progress with coments or slider percentages.
  - There is a deadline, employee and manager get email reminders
  - Manager can add comments to the onboarding document / review


- Org Maturity:
  - Define model per team or whole org - start from template
  - Preview current state
  - Edit (Managers only)


- Exit interviews:
  - Create and manage a feedback form for exit interviews
  - Allow employee to fill-in the form
  - Analytics on forms by multiple employees, charts, etc.


- Guides for first-time Managers:
  - Tutorials
  - Checklists
  - Best Practices


- Teambuilding / Swag / Motivation Ideas:
  - Cool Stickers
  - Cool Teambuilding games and excercises
  - Slack / Teams emoticons?


- Library & Training?:
  - List of library books
  - List of tutorial courses and skills


- Analytics
  - Most active employees on the platform
  - Total active employees, total number of days used, etc.
  - Scoreboard for recognition
  - Chart for personality profiles
