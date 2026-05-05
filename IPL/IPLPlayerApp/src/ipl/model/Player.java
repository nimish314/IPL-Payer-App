package ipl.model;

/**
 * Abstract base class representing an IPL Player.
 * Demonstrates ABSTRACTION and ENCAPSULATION.
 *
 * Common attributes are encapsulated as protected fields and exposed via getters/setters.
 * Two abstract methods enforce a contract on every subclass.
 */
public abstract class Player {

    protected String  playerId;
    protected String  name;
    protected String  country;
    protected String  team;
    protected int     age;
    protected double  basePrice;   // in INR Crores

    // ------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------
    public Player() { }

    public Player(String playerId, String name, String country,
                  String team, int age, double basePrice) {
        this.playerId  = playerId;
        this.name      = name;
        this.country   = country;
        this.team      = team;
        this.age       = age;
        this.basePrice = basePrice;
    }

    // ------------------------------------------------------------------
    // Abstract methods – must be implemented by every subclass
    // ------------------------------------------------------------------
    /** Compute a weighted performance score (higher = better). */
    public abstract double getPerformanceScore();

    /** Return role label such as "Batsman", "Bowler", "All-Rounder". */
    public abstract String getRole();

    /** Convert to pipe-delimited line for file persistence. */
    public abstract String toFileString();

    // ------------------------------------------------------------------
    // Getters / Setters (Encapsulation)
    // ------------------------------------------------------------------
    public String getPlayerId()                 { return playerId; }
    public void   setPlayerId(String playerId)  { this.playerId = playerId; }

    public String getName()                     { return name; }
    public void   setName(String name)          { this.name = name; }

    public String getCountry()                  { return country; }
    public void   setCountry(String country)    { this.country = country; }

    public String getTeam()                     { return team; }
    public void   setTeam(String team)          { this.team = team; }

    public int    getAge()                      { return age; }
    public void   setAge(int age)               { this.age = age; }

    public double getBasePrice()                { return basePrice; }
    public void   setBasePrice(double price)    { this.basePrice = price; }

    @Override
    public String toString() {
        return name + " (" + getRole() + ")";
    }
}
