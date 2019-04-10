#include <stdio.h>
#include <stdlib.h>

void queens(int);
int good_col(int, int, int*);
int good_diags(int, int, int*);
void print_forward(int*);
void print_backward(int*);

int num;
int comparisons = 0;
int solutions = 0;
int* used_cols;

int main() {
    int n;
    printf("Enter a board size: ");
    scanf("%d", &n);
    printf("%d\n", n);

    num = n;
    queens(n);
}

void queens(int num) {
    int* arr = malloc(sizeof(int) * num);
    used_cols = malloc(sizeof(int) * num);
    if (arr == NULL || used_cols == NULL) {
        printf("Somehow don't have enough memory. Huh?");
        exit(1);
    }

    int i, j, k;
    int done = 0;

    // Initialize the arrays
    for (i = 0; i < num; i++) {
        arr[i] = 0;
        used_cols[i] = 0;
    }

    int init_col = 0;
    // Loop over each row.
    for (i = 0; i < num && done == 0; i++) {
        printf("Now looking at row %d\n", i);
        int placed = 0;
        // Iterate over columns to place queen, starting at 0.
        // 'placed' variable acts as a boolean flag, continue to next row on 1
        for (j = init_col; j < num && placed == 0; j++) {
            // Check previous rows/columns/diagonals. 
            if (!used_cols[j] && good_diags(i, j, arr)) {
                arr[i] = j;
                // ERASE
                printf("Found good col. Row %d col %d\n", i, j);
                // Last row, successful board. Note we don't set placed to 1
                // so we can keep searching for more solutions.
                if (i == num - 1) {
                    solutions++;
                    print_forward(arr);
                    // If n > 1, we can flip the board horizontally for another sol.
                    // We don't automatically flip ones with the initial column in the
                    // middle because those will still be searched through
                    // shold be able to remove num > 1 condition because it's middle too
                    if (num > 1 && num / 2 != arr[0]) {
                        solutions++;
                        print_backward(arr);
                    }
                } else { // Successful row but not final row
                    placed = 1;
                    used_cols[j] = 1;
                }
            }
        }

        // No placement made - invalid row. Time to backtrack, so we decrement.
        if (placed == 0 && i > 0) {
            // ERASE
            printf("Backtracking... before deletion, we have arr %d %d %d %d   ... used %d %d %d %d\n", arr[0], arr[1], arr[2], arr[3], used_cols[0], used_cols[1], used_cols[2], used_cols[3]);
            
            // Decrement until we find a row that we can go back to
            // loop construct's increment will take us forward one.
            int prev_row = i - 1;
            used_cols[arr[prev_row]] = 0;
            printf("\tDeleting column %d from used\n", arr[prev_row]);

            if (arr[prev_row] == num - 1) {
                comparisons++;
                prev_row--;
                printf("\t\talso deleting column %d\n", arr[prev_row]);
                used_cols[arr[prev_row]] = 0;
            }

            i = prev_row - 1;  
            init_col = arr[prev_row] + 1;

            if (prev_row == 0 && init_col >= (num + 1) / 2) {
                done = 1;
            }
            // ERASE 
            printf("After deletion, we have arr %d %d %d %d   ... used %d %d %d %d\n", arr[0], arr[1], arr[2], arr[3], used_cols[0], used_cols[1], used_cols[2], used_cols[3]);
        } else {
            init_col = 0;
        }
    }

    if (solutions == 0) {
        printf("No solution for a %d-dimensional board.\n", num);
    } else {
        printf("%d solutions have been found\n", solutions);
    }

    printf("Number of array entry comparisons performed: %d\n", comparisons);
}

int good_diags(int row, int col, int* arr) {
    int i;
    int max_translate = row > col ? row : col;
    for (i = 1; i <= max_translate; i++) {
        comparisons++;
        if (i <= col && i <= row && arr[row - i] == col - i) {
            return 0;
        } 

        comparisons++;
        if (i + col < num && i <= row && arr[row - i] == col + i) {
            return 0;
        }
    }
    return 1;
}

void print_forward(int* arr) {
    int i, j;
    printf("Visual solution %d for a %d-dimensional board: \n", 
            solutions, num);
    for (i = 0; i < num; i++) {
        for (j = 0; j < arr[i]; j++)
            printf("* ");
        printf("Q ");
        for (j = arr[i]; j < num - 1; j++)
            printf("* ");
        printf("\n");
    }
    printf("\n");
}

void print_backward(int* arr) {
    int i, j;
    printf("Visual solution %d for a %d-dimensional board: \n", 
            solutions, num);
    for (i = 0; i < num; i++) {
        for (j = arr[i]; j < num - 1; j++)
            printf("* ");
        printf("Q ");
        for (j = 0; j < arr[i]; j++)
            printf("* ");
        printf("\n");
    }
    printf("\n");
}

