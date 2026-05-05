package ipl.model;

/**
 * Batsman – extends Player.
 * Demonstrates INHERITANCE, POLYMORPHISM (method overriding), and use of super().
 */
public class Batsman extends Player {

    private int    matches;
    private int    runs;
    private double average;
    private double strikeRate;
    private int    fifties;
    private int    hundreds;
    private int    sixes;
    private int    fours;

    // ------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------
    public Batsman() { super(); }

    public Batsman(String playerId, String name, String country, String team,
                   int age, double basePrice,
                   int matches, int runs, double average, double strikeRate,
                   int fifties, int hundreds, int sixes, int fours) {
        super(playerId, name, country, team, age, basePrice);
        this.matches    = matches;
        this.runs       = runs;
        this.average    = average;
        this.strikeRate = strikeRate;
        this.fifties    = fifties;
        this.hundreds   = hundreds;
        this.sixes      = sixes;
        this.fours      = fours;
    }

    // ------------------------------------------------------------------
    // Polymorphic behaviour
    // ------------------------------------------------------------------
    @Override
    public double getPerformanceScore() {
        // Score = (Avg × 0.30) + (SR × 0.25) + (Runs/Match × 0.20)
        //       + (50s × 2 × 0.15) + (100s × 5 × 0.10)
        double runsPerMatch = (matches == 0) ? 0 : (double) runs / matches;
        double score =
                (average     * 0.30) +
                (strikeRate  * 0.25) +
                (runsPerMatch * 0.20) +
                (fifties  * 2 * 0.15) +
                (hundreds * 5 * 0.10);
        return Math.round(score * 100.0) / 100.0;   // 2-decimal precision
    }

    @Override
    public String getRole() { return "Batsman"; }

    @Override
    public String toFileString() {
        // BAT|id|name|country|team|age|basePrice|matches|runs|avg|sr|50s|100s|6s|4s
        return String.join("|",
                "BAT",
                playerId, name, country, team,
                String.valueOf(age),
                String.valueOf(basePrice),
                String.valueOf(matches),
                String.valueOf(runs),
                String.valueOf(average),
                String.valueOf(strikeRate),
                String.valueOf(fifties),
                String.valueOf(hundreds),
                String.valueOf(sixes),
                String.valueOf(fours));
    }

    /** Static factory – reconstruct a Batsman from a file line. */
    public static Batsman fromString(String line) {
        String[] p = line.split("\\|", -1);
        return new Batsman(
                p[1], p[2], p[3], p[4],
                Integer.parseInt(p[5]),
                Double.parseDouble(p[6]),
                Integer.parseInt(p[7]),
                Integer.parseInt(p[8]),
                Double.parseDouble(p[9]),
                Double.parseDouble(p[10]),
                Integer.parseInt(p[11]),
                Integer.parseInt(p[12]),
                Integer.parseInt(p[13]),
                Integer.parseInt(p[14]));
    }

    // ------------------------------------------------------------------
    // Getters / Setters
    // ------------------------------------------------------------------
    public int    getMatches()    { return matches; }
    public int    getRuns()       { return runs; }
    public double getAverage()    { return average; }
    public double getStrikeRate() { return strikeRate; }
    public int    getFifties()    { return fifties; }
    public int    getHundreds()   { return hundreds; }
    public int    getSixes()      { return sixes; }
    public int    getFours()      { return fours; }

    public void setMatches(int m)         { this.matches = m; }
    public void setRuns(int r)            { this.runs = r; }
    public void setAverage(double a)      { this.average = a; }
    public void setStrikeRate(double s)   { this.strikeRate = s; }
    public void setFifties(int f)         { this.fifties = f; }
    public void setHundreds(int h)        { this.hundreds = h; }
    public void setSixes(int s)           { this.sixes = s; }
    public void setFours(int f)           { this.fours = f; }
}
