# ComplementaryArtificialMisuseExamples
a replication dataset for complementary artificial misuse examples


## Replicating our experiments
To replicate our work we recommend using [MuBench](https://github.com/stg-tud/MUBench)

### RQ1
Our findings for our RQ1 can be replicated by running [MuDetect](https://github.com/stg-tud/MUDetect) (also part of MuBench) using its cross-project setting on the 50 projects found in projectsUsedForFPDetection. We save the results (and the extracted 'correct' API usage examples.

Our approach (a link to a prototype tool can be found in our paper) to generate artificial misuse examples can then be run on the extracted 'correct' API usages to generate artificial alternative correct examples.
MuBench should the be re-run (this time using the existing 'correct' examples + the artificial alternative correct examples. The difference in results of the two runs (found in JSON_difference_with_without_aid.json) can then be compared to determine which misuses are part of only one set of results (those should be false positives).

### RQ2
For RQ2 we suggest runing the experiment ex1 (part of MuBench) with and without generating and using alternative examples on the MuBench dataset. The results can then be compared on those two runs. Our results for this experiment are found in Results.xlsx.