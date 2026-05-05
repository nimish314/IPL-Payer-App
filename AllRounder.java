package ipl.model;

/**
 * AllRounder – extends Player. Combines batting + bowling attributes.
 * Score is the average of batting and bowling sub-scores (equal weight).
 */
public class AllRounder extends Player {

    // Batting
    private int    matches;
    private int    runs;
    private double battingAverage;
    private double battingStrikeRate;
    private int    fifties;
    private int    hundreds;

    // Bowling
    private int    wickets;
    private double economy;
    private double bowlingAverage;
    private int    fourWickets;
    private int    fiveWickets;

    public AllRounder() { super(); }

    public AllRounder(String playerId, String name, String country, String team,
                      int age, double basePrice,
                      int matches, int runs, double battingAverage,
                      double battingStrikeRate, int fifties, int hundreds,
                      int wickets, double economy, double bowlingAverage,
                      int fourWickets, int fiveWickets) {
        super(playerId, name, country, team, age, basePrice);
        this.matches            = matches;
        this.runs               = runs;
        this.battingAverage     = battingAverage;
        this.battingStrikeRate  = battingStrikeRate;
        this.fifties            = fifties;
        this.hundreds           = hundreds;
        this.wickets            = wickets;
        this.economy            = economy;
        this.bowlingAverage     = bowlingAverage;
        this.fourWickets        = fourWickets;
        this.fiveWickets        = fiveWickets;
    }

    @Override
    public double getPerformanceScore() {
        double runsPerMatch = (matches == 0) ? 0 : (double) runs / matches;
        double wktsPerMatch = (matches == 0) ? 0 : (double) wickets / matches;
        double ecoFactor    = Math.max(0, 12 - economy);

        double battingScore =
                (battingAverage    * 0.30) +
                (battingStrikeRate * 0.25) +
                (runsPerMatch      * 0.20) +
                (fifties  * 2 * 0.15) +
                (hundreds * 5 * 0.10);

        double bowlingScore =
                (wickets * 1.5 * 0.30) +
                (ecoFactor * 3 * 0.25) +
                (wktsPerMatch * 10 * 0.25) +
                (fourWickets * 0.10) +
                (fiveWickets * 0.10);

        double score = (battingScore + bowlingScore) / 2.0;
        return Math.round(score * 100.0) / 100.0;
    }

    @Override
    public String getRole() { return "All-Rounder"; }

    @Override
    public String toFileString() {
        return String.join("|",
                "AR",
                playerId, name, country, team,
                String.valueOf(age),
                String.valueOf(basePrice),
                String.valueOf(matches),
                String.valueOf(runs),
                String.valueOf(battingAverage),
                String.valueOf(battingStrikeRate),
                String.valueOf(fifties),
                String.valueOf(hundreds),
                String.valueOf(wickets),
                String.valueOf(economy),
                String.valueOf(bowlingAverage),
                String.valueOf(fourWickets),
                String.valueOf(fiveWickets));
    }

    public static AllRounder fromString(String line) {
        String[] p = line.split("\\|", -1);
        return new AllRounder(
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
                Double.parseDouble(p[14]),
                Double.parseDouble(p[15]),
                Integer.parseInt(p[16]),
                Integer.parseInt(p[17]));
    }

    // Getters / Setters
    public int    getMatches()           { return matches; }
    public int    getRuns()              { return runs; }
    public double getBattingAverage()    { return battingAverage; }
    public double getBattingStrikeRate() { return battingStrikeRate; }
    public int    getFifties()           { return fifties; }
    public int    getHundreds()          { return hundreds; }
    public int    getWickets()           { return wickets; }
    public double getEconomy()           { return economy; }
    public double getBowlingAverage()    { return bowlingAverage; }
    public int    getFourWickets()       { return fourWickets; }
    public int    getFiveWickets()       { return fiveWickets; }

    public void setMatches(int m)               { this.matches = m; }
    public void setRuns(int r)                  { this.runs = r; }
    public void setBattingAverage(double a)     { this.battingAverage = a; }
    public void setBattingStrikeRate(double s)  { this.battingStrikeRate = s; }
    public void setFifties(int f)               { this.fifties = f; }
    public void setHundreds(int h)              { this.hundreds = h; }
    public void setWickets(int w)               { this.wickets = w; }
    public void setEconomy(double e)            { this.economy = e; }
    public void setBowlingAverage(double b)     { this.bowlingAverage = b; }
    public void setFourWickets(int f)           { this.fourWickets = f; }
    public void setFiveWickets(int f)           { this.fiveWickets = f; }
}
