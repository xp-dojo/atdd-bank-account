# Instructor Notes

## Session Introduction

There are [slides](slides.pptx) to talk along with. This is really about introducing some of the ideas around ATDD. In particular:

* Introduction acceptance testing as the "outer cycle" to the red-green-refactor/TDD cycle.
* De-coupling executable fixtures from "specifications"
* Abstracting the meaning of "done" out of the specifications and into fixture and test code (insulating the specifications)
* A bit of background on Concordion and the basic mechanics (there's an example of an acceptance already in the code base to refer students too)


## Wrap-up

### 

It's important to show a finished example at the end of the session.

You should show a worked example at difference stages to:

* Show how a partial acceptance test (lets say without interest accrued or even just deposits and not withdrawals)

THe learning point here is about not taking on too much and instead breaking the ATDD loop into smaller **iterative** loops, each concluding with a **customer demo** and **improved understanding** of the requirements. Remind people about the difference between _iterative_ and _incremental_.

> Who put the address on the statement? Was it the customer address? or the branch address? Both? Who asked for that? How did you clarify your understanding? 


### "Changing Gears"

Make sure you describe the steps: 1) write a failing acceptance test, 2) go straight into the TDD until 3) the acceptance test passes.

In Concodrion, you can use `@Unimplemented` or `@ExpectedToFail` annotations which going round the loop.

![](images/tdd-with-acceptance-tests.svg)