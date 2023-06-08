import java.util.Scanner;

public class Main {
    private static char[][] board;
    private static final int SIZE = 3; // розмір ігрового поля
    private static final char EMPTY = '-'; // символ для позначення порожньої клітинки
    private static final char HUMAN_PLAYER = 'X'; // символ для позначення ходів гравця-людини
    private static final char COMPUTER_PLAYER = 'O'; // символ для позначення ходів гравця-комп'ютера

    public static void main(String[] args) {
        initializeBoard();
        playGame();
    }

    // ініціалізація гри до початкового стану
    private static void initializeBoard() {
        board = new char[SIZE][SIZE]; // новий двовимірний масив board розміром SIZE x SIZE (ігрова дошка)
        for (int i = 0; i < SIZE; i++) { //  виконується для кожного рядка
            for (int j = 0; j < SIZE; j++) { // виконується для кожного стовпця в поточному рядку
                board[i][j] = EMPTY; // встановлюється значення EMPTY (порожньої клітинки) для кожної комірки board[i][j]
            }
        }
    }

    // управління процесом гри
    private static void playGame() {
        System.out.println("Гра розпочалась! Ви граєте хрестиками (X).");
        System.out.println("Введіть рядок та стовпець для ходу у форматі 'рядок стовпець' (наприклад, '0 2').");
        printBoard(); //  викликає метод printBoard(), який виводить поточний стан гральної дошки

        Scanner scanner = new Scanner(System.in);

        while (!isBoardFull()) { // буде виконуватися, поки дошка не буде повністю заповнена
            // Хід гравця
            if (makePlayerMove(scanner)) { // перевіряє, чи гравець зробив хід
                break;
            }

            // Хід комп'ютера
            if (makeComputerMove()) { // перевіряє, чи комп'ютер зробив хід
                break;
            }
        }

        System.out.println("Гра завершилась!");
    }

    // обробка ходу гравця під час гри
    private static boolean makePlayerMove(Scanner scanner) {
        System.out.print("Ваш хід: ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();

        if (!isValidMove(row, col)) { // перевіряє, чи введений гравцем хід є дійсним
            System.out.println("Недійсний хід. Спробуйте ще раз.");
            return false;
        }

        board[row][col] = HUMAN_PLAYER; //  встановлює символ гравця (HUMAN_PLAYER) у вибрану клітинку на дошці
        printBoard(); // викликає метод printBoard(), який виводить оновлений стан гральної дошки після ходу гравця

        if (isWinningMove(HUMAN_PLAYER)) { // перевіряє, чи хід гравця (HUMAN_PLAYER) є виграшним
            System.out.println("Ви перемогли!");
            return true;
        } else if (isBoardFull()) { // перевіряє, чи всі клітинки заповнені
            System.out.println("Нічия!");
            return true;
        }

        return false;
    }

    // обробка ходу комп'ютера під час гри
    private static boolean makeComputerMove() {
        System.out.println("Хід комп'ютера:");

        int bestScore = Integer.MIN_VALUE; // змінна для збереження найкращого результату, отриманого під час виконання алгоритму
        int bestRow = -1; // змінна для збереження рядка на дошці, де комп'ютер має зробити найкращий хід
        int bestCol = -1; // змінна для для збереження стовпця на дошці, де комп'ютер має зробити найкращий хід

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j)) { // перевіряє, чи клітинка є вільною та знаходиться на дошці
                    board[i][j] = COMPUTER_PLAYER; // встановлює символ комп'ютера (COMPUTER_PLAYER) у вибрану клітинку на дошці для оцінки ходу

                    int score = minimax(board, 0, false); // викликає алгоритм minimax для оцінки ходу комп'ютера

                    board[i][j] = EMPTY; // відновлює вибрану клітинку на пусте значення, щоб зберегти початковий стан дошки після оцінки ходу

                    if (score > bestScore) { // перевіряє, чи отриманий результат оцінки ходу (score) кращий за поточний найкращий результат (bestScore)
                        // якщо так, то оновлюються значення bestScore, bestRow і bestCol
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        board[bestRow][bestCol] = COMPUTER_PLAYER; // встановлює символ комп'ютера (COMPUTER_PLAYER) у клітинку на дошці, де комп'ютер робить найкращий хід
        printBoard(); // виводить оновлений стан гральної дошки після ходу комп'ютера

        if (isWinningMove(COMPUTER_PLAYER)) { // перевіряє, чи хід комп'ютера (COMPUTER_PLAYER) є виграшним
            System.out.println("Комп'ютер переміг!");
            return true;
        } else if (isBoardFull()) {
            System.out.println("Нічия!");
            return true;
        }

        return false;
    }

    // алгоритм Мінімакс для прийняття оптимального рішення комп'ютером у грі "хрестики-нулики"
    private static int minimax(char[][] board, int depth, boolean isMaximizingPlayer) {
        if (isWinningMove(HUMAN_PLAYER)) { // перевіряє, чи хід людини (HUMAN_PLAYER) є виграшним
            return -1; //якщо так
        } else if (isWinningMove(COMPUTER_PLAYER)) { // перевіряє, чи хід комп'ютера (COMPUTER_PLAYER) є виграшним
            return 1; // якщо так
        } else if (isBoardFull()) { // перевіряє, чи всі клітинки заповнені
            return 0;
        }

        if (isMaximizingPlayer) { // перевіряє, чи зараз потрібно максимізувати оцінку гравця
            int bestScore = Integer.MIN_VALUE; // збереження найкращого результату, отриманого під час виконання алгоритму

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isValidMove(i, j)) { // перевіряє, чи можна зробити хід у клітинку з координатами (i, j)
                        board[i][j] = COMPUTER_PLAYER; // встановлює символ комп'ютера (COMPUTER_PLAYER) у клітинку на дошці

                        int score = minimax(board, depth + 1, false); // ядок рекурсивно викликає метод minimax() з оновленою дошкою, збільшеним рівнем глибини (depth + 1) і змінною isMaximizingPlayer встановленою на false

                        board[i][j] = EMPTY; //  відновлює вибрану клітинку на пусте значення, щоб зберегти початковий стан дошки після оцінки ходу

                        bestScore = Math.max(score, bestScore); // оновлює значення bestScore, вибираючи більше значення між поточним bestScore і score
                    }
                }
            }

            return bestScore;
        } else { // потрібно мінімізувати оцінку гравця
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isValidMove(i, j)) {
                        board[i][j] = HUMAN_PLAYER;

                        int score = minimax(board, depth + 1, true);

                        board[i][j] = EMPTY;

                        bestScore = Math.min(score, bestScore);
                    }
                }
            }

            return bestScore;
        }
    }

    // перевіряє, чи поточний хід є дійсним
    private static boolean isValidMove(int row, int col) {
        // перевіряє, чи значення row та col знаходяться в межах допустимого діапазону координат дошки. Якщо row та col менше 0 або більше або дорівнюють розміру дошки SIZE, то хід вважається недійсним
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY;
    }

    // перевіряє, чи дошка повністю заповнена
    private static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    // якщо клітинка порожня, то повертає false
                    return false;
                }
            }
        }
        return true;
    }

    // перевіряє, чи зроблений хід є переможним
    private static boolean isWinningMove(char player) {
        for (int i = 0; i < SIZE; i++) {
            // перевірка рядків і стовпців
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        // перевірка діагоналей
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    // виводить дошку на екран
    private static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
