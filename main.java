import java.util.*;

class CYKAlgorithm {

    // Grammar stored as: RHS -> {LHS}
    static Map<String, Set<String>> grammar = new HashMap<>();

    // Add a production
    static void addProduction(String lhs, String rhs) {
        grammar.computeIfAbsent(rhs, k -> new HashSet<>()).add(lhs);
    }

    // Initialize CNF Grammar
    static void initializeGrammar() {

        // Binary productions
        addProduction("S0", "AC");
        addProduction("C", "XB");
        addProduction("X", "RY");
        addProduction("Y", "PZ");
        addProduction("Z", "XP");

        // Terminal productions
        addProduction("X", "q");
        addProduction("X", "r");
        addProduction("Y", "p");
        addProduction("A", "(");
        addProduction("B", ")");
        addProduction("R", "r");
        addProduction("P", "p");
    }

    // CYK Algorithm
    static boolean cykParse(String word) {
        int n = word.length();
        Set<String>[][] table = new HashSet[n][n];

        // Initialize table
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table[i][j] = new HashSet<>();
            }
        }

        // Fill diagonal (terminals)
        for (int i = 0; i < n; i++) {
            String terminal = String.valueOf(word.charAt(i));
            if (grammar.containsKey(terminal)) {
                table[i][i].addAll(grammar.get(terminal));
            }
        }

        // Fill table
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;

                for (int k = i; k < j; k++) {
                    for (String b : table[i][k]) {
                        for (String c : table[k + 1][j]) {
                            String rhs = b + c;
                            if (grammar.containsKey(rhs)) {
                                table[i][j].addAll(grammar.get(rhs));
                            }
                        }
                    }
                }
            }
        }

        return table[0][n - 1].contains("S0");
    }

    // Main method
    public static void main(String[] args) {

        initializeGrammar();

        String[] acceptedWords = {"(q)", "(r)", "(rp)", "(rpqp)"};
        String[] rejectedWords = {"()", "q", "(p)", "(rq)", "(qp)", "(pq)", "(rpq)"};

        System.out.println("=== ACCEPTED WORDS ===");
        for (String w : acceptedWords) {
            System.out.println(w + " : " + (cykParse(w) ? "Accepted" : "Rejected"));
        }

        System.out.println("\n=== REJECTED WORDS ===");
        for (String w : rejectedWords) {
            System.out.println(w + " : " + (cykParse(w) ? "Accepted" : "Rejected"));
        }
    }
}
