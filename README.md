# Performity
## Where? (DEMO)
You can access the Demo environment here.
TODO https://dundermifflin.performity.app/

Credentials:

Manager: michael.scott@dundermifflin.com / Pass123$%^&

Team Member: jim.halpert@dundermifflin.com / Pass123$%^&


## What?
Performity is a toolbox for managers and their teams. It provides invalueable tools for team management, collaboration, career growth, recognition and improving engagement.

### Performity for Managers
 - Vacation Planner - request and approve time off (Upcoming)
 - On-call - schedule on-call calendar (Upcoming)
 - Define Position Levels and Career Paths; Track employee progress; Define equal salary bands. (Upcoming)
 - Define and share Team / Company values. (Upcoming)
 - Recognition - recognize good performance and demonstration of company values; Track best performance in the team. (Upcoming)
 - Connect with teammembers - a common platform for Q&A and a suggestion box. (Upcoming)
 - Employee satisfaction - see what drives your teammembers; provide them with a platform to express their opinion and suggestions. (Upcoming)
 - Performance Reviews - get a dedicated platform for doing performance reviews, accomodating industry leading techniques and processes for performance reviews. (Upcoming)
 - 1-2-1 Meetings - a guide and templates, on how to perform one to one meetings, saving you a ton of preparation time. (Upcoming)
 - Employee engagement - measure how engaged your employees are with the company Invaluable indicator for commitment and employee churn. (Upcoming)
 - Onboarding - create and track teammember onboarding plans. (Upcoming)
 - Org Maturiy - design, track progress and improve the maturity of your team. (Upcoming)
 - Guides for 1st time Managers. (Upcoming)
 - Exit interviews - get feedback on why teammates are leaving. (Upcoming)
 - Analytics - see how people are using the platform. (Upcoming)


### Performity for the Team
 - Team availability calendar and PTO planning. (Upcoming)
 - On-call - see on-call schedule. (Upcoming)
 - Connect - common platform for Q&A, even fully annonimous questions / suggestions. (Upcoming)
 - Recognition - your contribution won't remain unnoticed; you can recognize your teammates and thank them appropriately. (Upcoming)
 - Employee satisfaction - make sure your opinion and suggestions are heard. (Upcoming)
 - Career Path - clearly see where you stand in the organization, what are the possible paths ahead, and what is required to move into the next position you desire. (Upcoming)
 - Performance Reviews - get timely and detailed performance reviews and feedback from your manager, against OKRs and using objective criteria. See your good qualities and where you need to improve. (Upcoming)
 - Onboarding - new starters on the team, get a great platform with a checklist for their onboarding process, and regular feedback on how they are doing. (Upcoming)


## Why?
Performity combines two passions of mine - people management and software development. During my years as an Engineering Manager, I have used several tools for people management, but none of them were good enough or provided all the functionality that I needed. That is why I decided to build Performity from the ground up.


# Development and Contribution
I love and support open-source, so the core part of Performity is released as an open-source project, under to GPL3 license. You are free to use and extend it, as long as you keep the license.

If you are interested in contributing to the development of the product, please read below.

## Tech Stack
TODO

## Development and Coding Standards
This project follows certain coding standards, in order to assert high-quality of the code. All pull-requests must follow those standards.

#### Local Dev Setup
TODO:
git clone
./gradlew build
docker-compose up
checkstyle setup
sonarlint setup
prettier setup
pre-commit hook
etc.


#### Submitting a new PR
There are certain automated checks that need to pass for every PR:
 - checkstyle - executed automatically as a pre-commit hook
 - sonarlint - executed automatically as a pre-commit hook
 - unit tests - executed automatically as a pre-commit hook

Please, add a good description of your PR and link it to an issue.


## Architecture
Performity follows a distributed microservice architecture.
Here is a basic architecture diagram:
![Architecture Diagram](docs/images/Architecture_Diagram_1.png?raw=true "Architecture Diagram")


#### Inter-Service Communication
Services talk to each other in 2 main ways:
 - Synchronious - using REST api calls. Used when the service cannot proceed with the current request, until the other service responds - ie blocking. 
 - Asyncrhonious - using Kafka messages - used when the service needs to notify other services, but can otherwise continue with the processing of the current request - ie non-blocking.

All services must use service discovery, no hardcoded addresses for communication.

In order to facility traceability of requests through a microservice, each service needs to ...

#### Service Inner Architecture
TODO - hexagonnal, onion, clean arch, etc.

#### Authentication & Authorization
TODO

#### Logging
TODO

#### DB & Persistance
TODO - multitenancy




## Quality Assurance
Performity takes Quality seriously.
Every change must pass through a rigorous verification, before being merged.
Extensive automated testing is done, in order to assure no serious issues exists, to allow for better maintainability of the code, and more confident changes.


#### Test Types
Performity follows the testing pyramid philosophy, which dictates that we use mostly unit tests, as they are the fastest and most reliable; fewer integration tests and even less end-to-end tests.


#### Test Coverage Guide
Performity does not enforce any coverage numbers, but leaves it to the developer to decide what exactly needs to be covered.
However, the following guidelines are followed:
1. Service Layer - try to thoroughly cover all Services with unit tests. The services contain the business logic of the application, and therefore are critical to be properly tested. Business logic is the most important logic and also it is the easiest to miss a bug in the business logic. Usually, thorough testing of a service method means multiple unit tests, in order to cover different scenarios and input data. The coverage guide is valid for both BackEnd and FrontEnd service classes. Technology - JUnit5 and Mockito for BE, Jest for FE.
2. REST Controllers - try to have at least 1 test per controller. We need to verify that the routing logic is correct. If the controller is doing authentication, you must check that it was properly invoked. If the controller is parsing input data (ex. JSON), you must verify that the mapping is done correctly, and also that verification of the data is done using proper JSON schema. Technology - JUnit5 and Mockito.
3. Repository tests - the developer should determine what exactly needs to be covered. If there is any custom logic used on top of Spring JPA, it generally should be covered. Technology - JUnit5 and Mockito.
4. FrontEnd Views - try to have at least 1 snapshot test for each view. Technology - Jest.
5. Integration tests - as a general rule of thumb - if there is a request that is processed by more than 1 microservice syncrhoniously, it needs to have at least 1 contract test. Technology - TBD
6. End-to-end tests - those are the highest level tests and the fewest. They only cover the most important positive scenarios for the application, and generally involve several actions, in order to achieve a given use case for the application. They need to have the whole application up and running and simulate a real user using the application. Each feature of the application needs to have at least 1 end-to-end test. Technology - playwright with typescript.



## DevOps
Performity follows a fully automated CI/CD process. Deploying new versions can happen automatically.

#### Deployment
Performity uses Docker containers running on k8s for deployment. 


#### High-Availability
The k8s cluster can automatically scale horizontally any of the services, if needed, by running additional containers of that service. Since using service discovery (Consul), the new containers automatically register (and de-register when stopped) with Consul, and can be used for load distribution.
There is no single-point of failure.

The k8s cluster is setup with liveness probes, so that it monitors the services constantly and if any one of them is down, it will restart the container.

#### Rolling Deployment
The k8s cluster is setup to deploy new versions using the Rolling Deployment strategy. This assures safe deployments with no downtime. If the new version starts throwing errors, the startegy will revert the deployment.
