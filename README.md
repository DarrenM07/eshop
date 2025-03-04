# eshop
***ASSIGNMENT 1***

**Reflection 1**

In developing the EShop application, I applied clean code principles such as meaningful naming conventions (e.g., productId), and followed the Single Responsibility Principle (SRP) by separating concerns between the controller, service, and repository layers. I also reduced code duplication by reusing logic within the service and repository. For secure coding, I added confirmation prompts for delete actions to prevent accidental deletions. Areas for improvement include adding input validation with annotations like @NotNull and @Size, implementing custom error pages for better error handling, and adding authorization checks to secure edit and delete endpoints. Moving forward, I plan to integrate unit testing with JUnit and connect the application to a database for persistent data storage.

**Reflection 2**

## 1) How do you feel after writing the unit test?
Honestly, writing unit tests can feel a bit tedious at first, but it's also kinda satisfying when everything runs without errors. It gives a sense of confidence that the code is actually working as expected. Plus, seeing all tests pass is lowkey addictive—feels like winning a mini battle in coding.

## 2)How many unit tests should be made in a class?
There’s no fixed number, but the idea is to test all possible scenarios—both positive (expected behavior) and negative (edge cases, invalid inputs, etc.). Every method that contains some logic should ideally have at least one test case, but if there are multiple conditions, we should test them all.

## 3)How do we make sure our unit tests are enough?
A good way to check is by using code coverage tools like JaCoCo. These tools show what percentage of the code is actually being tested. But having 100% coverage doesn’t mean the code is bug-free—it just means every line was executed during testing. We still need to consider edge cases, unexpected inputs, and real-world scenarios to make our tests meaningful.

***ASSIGNMENT 2***

## 1)
During the exercise, I addressed several code quality issues, notably replacing the repeated literal string "redirect:/product/list" with a constant (REDIRECT_PRODUCT_LIST) to eliminate redundancy and improve maintainability. I also identified and filled test coverage gaps by adding tests for edge cases, such as when a product’s ID is null during the edit flow, ensuring that all controller methods have 100% code coverage. Moreover, the CI/CD pipeline is set up to automatically run test suites, analyze code quality, and deploy the application to a PaaS, which embodies the principles of Continuous Integration and Continuous Deployment. Every commit triggers these automated processes, catching issues early and ensuring that successful builds are seamlessly deployed to production without manual intervention, keeping the software in a reliably deployable state at all times.


***ASSIGNMENT 3***

## 1) Principles Applied:  
In this project, I focused on three main SOLID principles: SRP, LSP, and DIP. For SRP (Single Responsibility Principle), every class is designed to do one thing only—like how the ProductRepository only handles data access and ProductService only manages business logic. With LSP (Liskov Substitution Principle), our code relies on abstractions so that any repository implementation that meets the contract can be swapped in without breaking the app. Finally, DIP (Dependency Inversion Principle) means that higher-level modules, like our services, depend on interfaces rather than concrete classes, which is why we use dependency injection to keep things loose and flexible.

## 2) Advantages of Applying These Principles:  
Using SRP, LSP, and DIP makes our code way more maintainable and easier to test. For instance, if I need to change how products are stored (say, moving from an in-memory list to a database), I only need to tweak the ProductRepository without touching the business logic in ProductService. Thanks to LSP, I can swap out repository implementations without worrying about breaking functionality, just like swapping out interchangeable parts on your ride. And with DIP, I can easily plug in mock repositories for testing, which saves me a ton of time debugging and ensures that changes in one layer don’t ripple through the whole project.

## 3) Disadvantages of Not Applying These Principles:  
If I didn’t stick to SRP, LSP, and DIP, the code would be a hot mess. Classes might end up doing too many things at once, making them super hard to maintain or update—a change in one part could unexpectedly break another. Without LSP, my services would be tightly coupled to specific implementations, so any change to a repository might force me to rewrite entire chunks of the service layer. And without DIP, I'd be stuck with direct dependencies between high-level and low-level modules, making testing a nightmare and future modifications nearly impossible. Overall, skipping these principles means more bugs, more headaches, and less scalable code.


***ASSIGNMENT 4***

## 1) Reflection on Test-Driven Development (TDD) Workflow

I tried out the TDD flow based on Percival (2017)’s self-reflective questions, and honestly, it was pretty helpful overall. Writing tests before coding forced me to think clearly about what my code should do, and it helped catch issues early. However, there were times when the tests felt a bit too rigid or made me change my implementation more than I’d like. Next time, I plan to spend more time upfront planning the test cases and making them flexible enough to accommodate refactoring. Overall, TDD has been a good learning curve—it keeps me focused and improves code quality, even if it can slow me down a bit initially.

## 2) Reflection on F.I.R.S.T. Principles in Unit Testing

My unit tests generally stick to the F.I.R.S.T. principles: they run fast, are independent from one another, and provide clear, self-validating results. This makes it super easy to pinpoint issues and keep the test suite reliable. That said, I’ve noticed some tests could be more robust with better assertions and additional coverage for edge cases. Next time, I'll work on refining each test to ensure it’s as atomic as possible and covers more scenarios, so that the tests are even more effective in catching bugs early on. This tweak should help in keeping my test suite lean, mean, and super reliable.