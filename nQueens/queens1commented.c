#include <stdio.h>
#include <stdlib.h>

void queens(int);
int good_col(int, int, int*);
int good_diags(int, int, int*);

int num;

int main() {
    int n;
    printf("Enter a board size (num of queens): ");
    scanf("%d", &n);
    printf("%d\n", n);

    num = n;
    queens(n);
}

void queens(int num) {
    int* arr = malloc(sizeof(int) * num);
    if (arr == NULL) {
        printf("Somehow don't have enough memory. Huh?");
        exit(1);
    }

    int i, j, k;

    // Initialize the array
    for (i = 0; i < num; i++) 
        arr[i] = 0;

    int init_col = 0;
    // Loop over each row.
    for (i = 0; i < num; i++) {
        int placed = 0;
        // printf("row %d\n", i);
        // Iterate over columns to place queen, starting at 0.
        // 'placed' variable acts as a boolean flag, continue to next row on 1
        for (j = init_col; j < num && placed == 0; j++) {
            // printf("\tcol %d\n", j);
            // Check previous rows/columns/diagonals. 
            if (good_col(i, j, arr) && good_diags(i, j, arr)) {
                // printf("\t\tvalid column\n");
                arr[i] = j;
                placed = 1;
            }
        }
        // No placement made - invalid row. Time to backtrack, so we decrement.
        if (placed == 0) {
            // Decrement until we find a row that we can go back to
            // loop construct's increment will take us forward one.
            int prev_row = i - 1;
            while (prev_row > 0 && arr[prev_row] == num - 1) {
                prev_row--;
            }
            i = prev_row - 1;  
            init_col = arr[prev_row] + 1;
            if (prev_row == 0 && init_col == num / 2) {
                printf("No solution found.\n");
                exit(1);
            }
        } else {
            init_col = 0;
        }
    }

    printf("[");
    // Theoretically we made it! Successful solution found.
    for (i = 0; i < num; i++) {
        printf("%d", arr[i]);
        if (i < num - 1) 
            printf(", ");
    }
    printf("]\n");
}

int good_col(int row, int col, int* arr) {
    int i;
    for (i = 0; i < row; i++) {
        if (arr[i] == col) {
            // printf("\tinvalid col\n");
            return 0;
        }
    }
    return 1;
}

int good_diags(int row, int col, int* arr) {
    int i;
    int max_translate = row > col ? row : col;
    for (i = 1; i <= max_translate; i++) {
        if ((i <= col && i <= row && arr[row - i] == col - i) || 
            (i + col < num && i <= row && arr[row - i] == col + i)) {
            // printf("\t\trow %d col %d i %d max %d\n", row, col, i, max_translate);
            // printf("\tinvalid diag\n");
            return 0;
        }
    }
    return 1;
}

