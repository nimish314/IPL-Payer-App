package ipl.model;

/**
 * Bowler – extends Player.
 * For bowlers, lower economy is better, so the score formula inverts it.
 */
public class Bowler extends Player {

    private int    matches;
    private int    wickets;
    private double economy;
    private double bowlingAverage;
    private double strikeRate;
    private int    fourWickets;
    private int    fiveWickets;

    public Bowler() { super(); }

    public Bowler(String playerId, String name, String country, String team,
                  int age, double basePrice,
                  int matches, int wickets, double economy,
                  double bowlingAverage, double strikeRate,
                  int fourWickets, int fiveWickets) {
        super(playerId, name, country, team, age, basePrice);
        this.matches        = matches;
        this.wickets        = wickets;
        this.economy        = economy;
        this.bowlingAverage = bowlingAverage;
        this.strikeRate     = strikeRate;
        this.fourWickets    = fourWickets;
        this.fiveWickets    = fiveWickets;
    }

    @Override
    public double getPerformanceScore() {
        // Score = (W × 1.5 × 0.30) + ((12 - Eco) × 3 × 0.25)
        //       + (W/Match × 10 × 0.25) + (4W × 0.10) + (5W × 0.10)
        double wPerMatch = (matches == 0) ? 0 : (double) wickets / matches;
        double ecoFactor = Math.max(0, 12 - economy);
        double score =
                (wickets * 1.5 * 0.30) +
                (ecoFactor * 3 * 0.25) +
                (wPerMatch * 10 * 0.25) +
                (fourWickets * 0.10) +
                (fiveWickets * 0.10);
        return Math.round(score * 100.0) / 100.0;
    }

    @Override
    public String getRole() { return "Bowler"; }

    @Override
    public String toFileString() {
        // BOWL|id|name|country|team|age|basePrice|matches|wickets|eco|bowlAvg|sr|4w|5w
        return String.join("|",
                "BOWL",
                playerId, name, country, team,
                String.valueOf(age),
                String.valueOf(basePrice),
                String.valueOf(matches),
                String.valueOf(wickets),
                String.valueOf(economy),
                String.valueOf(bowlingAverage),
                String.valueOf(strikeRate),
                String.valueOf(fourWickets),
                String.valueOf(fiveWickets));
    }

    public static Bowler fromString(String line) {
        String[] p = line.split("\\|", -1);
        return new Bowler(
                p[1], p[2], p[3], p[4],
                Integer.parseInt(p[5]),
                Double.parseDouble(p[6]),
                Integer.parseInt(p[7]),
                Integer.parseInt(p[8]),
                Double.parseDouble(p[9]),
                Double.parseDouble(p[10]),
                Double.parseDouble(p[11]),
                Integer.parseInt(p[12]),
                Integer.parseInt(p[13]));
    }

    // Getters / Setters
    public int    getMatches()        { return matches; }
    public int    getWickets()        { return wickets; }
    public double getEconomy()        { return economy; }
    public double getBowlingAverage() { return bowlingAverage; }
    public double getStrikeRate()     { return strikeRate; }
    public int    getFourWickets()    { return fourWickets; }
    public int    getFiveWickets()    { return fiveWickets; }

    public void setMatches(int m)              { this.matches = m; }
    public void setWickets(int w)              { this.wickets = w; }
    public void setEconomy(double e)           { this.economy = e; }
    public void setBowlingAverage(double b)    { this.bowlingAverage = b; }
    public void setStrikeRate(double s)        { this.strikeRate = s; }
    public void setFourWickets(int f)          { this.fourWickets = f; }
    public void setFiveWickets(int f)          { this.fiveWickets = f; }
}
