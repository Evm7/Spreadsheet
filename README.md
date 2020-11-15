# Spreadsheet
SpreadSheet project for the ArqSoft subject in MATT (Master in Advanced Telecomunications), ETSETB

## CODE EXPLANATION
  - Spreadsheet --> Contains a matrix with all the cells separated by column (Char[]) and row (Integer)
    - It is important to take into account that the Spreadsheet[0][0] = cell A1 (biase of 1 in both row and column)
    - SpreadSheet is the uniq class capable of referncying the values of the cells to the arguments
    - Spreadsheet uses the absolute nomenclautre of coordinates, then cells manage the bias.

  - Cell --> Each cell does contain the value, the content and the coordinates.
    - The content and the value can be from Number, Text or Formula.

  - Formula --> Formula does contain operant and operators, which can be obtained from the Arguments parsed from the String passed.

  - Argument --> It is the string which is written by the user and it is used to parse the formula and convert into operants.
    - There exist ArgumentRange : differnet reference to cells separated by : with square form --> A1:B2;
    - There exist ArgumentIndividual: uniq reference to cell:  A5.
    - There exist ArgumentFunction : does contain one of the functions : SUMA; MIN, MAX, PROMEDIO and its correspondent OperantFunction.
    - There exist ArgumentValue : contains a value as an string : "3"


  - Operant --> it is the terms that are operated by the operators.
    -It is important to take into account that there exists OperantNumbers (simple double) and OperantFunctions (set of OperantNumbers separated by ;)

  - Operators --> different signs that can operate the operants (+, -, *, /, ;)

  - ParseFormula --> Used to convert the initial String introduced by the user to set of arguments and operators, and sort them by the correspondent computational order (depends on brackets).

  - ContentFormula --> Used to compute the argument and operators and return the final result of the formula.

  - Functions --> Used to compute the particular function to the OperantFunction.

  - Operators --> Used to compute the particular operation between the neareset operants;
