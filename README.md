A Java + Swing OOP application built for the **Object-Oriented Programming**
course at Savitribai Phule Pune University.

It demonstrates **Abstraction, Inheritance, Polymorphism, Encapsulation, Custom Exceptions,
File I/O, Packages, and Swing GUI programming** — every topic from the syllabus, in one
working project.

---

## 📁 Project Structure

```
IPLPlayerApp/
├── src/
│   └── ipl/
│       ├── model/
│       │   ├── Player.java        ← Abstract base class
│       │   ├── Batsman.java       ← extends Player
│       │   ├── Bowler.java        ← extends Player
│       │   └── AllRounder.java    ← extends Player
│       ├── util/
│       │   ├── FileHandler.java   ← BufferedReader / BufferedWriter persistence
│       │   └── PlayerException.java ← Custom checked exception
│       └── gui/
│           └── IPLPlayerApp.java  ← Main JFrame / JTabbedPane / JTable application
├── players.txt                    ← Sample data (Virat, Rohit, Bumrah, Hardik)
└── README.md
```

---

## ✅ Prerequisites

1. **JDK 17 or later** (JDK 21 recommended).
   Verify in a terminal:
   ```bash
   java  -version
   javac -version
   ```
   If `javac` is missing you only have the JRE — install the **JDK** from
   <https://adoptium.net> or via your package manager.

2. **Visual Studio Code** with the **Extension Pack for Java** by Microsoft
   (this single pack installs *Language Support for Java*, *Debugger for Java*,
   *Test Runner for Java*, *Maven for Java*, *Project Manager for Java*, and
   *IntelliCode*).

---

## ▶️ How to Run in VS Code

### Option A — The easy way (recommended)

1. **Open the folder** `IPLPlayerApp` in VS Code (`File → Open Folder…`).
2. VS Code's Java extension will auto-detect the `src/` folder and prompt to
   *Import Java project*. Click **Yes**.
3. Open `src/ipl/gui/IPLPlayerApp.java`.
4. You will see a **▶ Run** | **🐞 Debug** code-lens directly above the
   `public static void main` line. **Click ▶ Run.**
5. The Swing window titled *"🏏 IPL Player Comparison & Analysis Tool"* opens.

That is it — VS Code handles compilation automatically and writes class files
to its `bin/` workspace folder.

### Option B — Command-line (works without any extensions)

From the `IPLPlayerApp/` folder:

```bash
# Compile every source file under src/ into bin/
javac -d bin -sourcepath src src/ipl/gui/IPLPlayerApp.java

# Run the main class (note the dotted package path, no .class)
java -cp bin ipl.gui.IPLPlayerApp
```

You can run these two commands from VS Code's integrated terminal
(`Ctrl + ` ` ` `).

---

## 🧪 Quick Demo Walk-through

`players.txt` ships with four sample records so you can test instantly:

1. **Tab "📋 All Players"** — see the 4 pre-loaded players with their
   computed Performance Scores.
2. **Tab "⚖ Compare Players"** — pick **Virat Kohli** and **Rohit Sharma**,
   click **Compare**. The table renders side-by-side stats and highlights
   Kohli's column in **gold** with the verdict line:
   *"🏆 Virat Kohli is the BETTER player with a Performance Score of … vs …"*
3. **Tab "➕ Add Player"** — switch the **Role** dropdown to see the form
   fields change dynamically. Try adding a Bowler and watch validation kick in
   when you leave a numeric field blank or non-numeric.
4. Re-open the app — your additions persist via `players.txt`.

> 💡 Want to start fresh? Delete `players.txt` (or empty it) before launching.

---

## 🧠 OOP Concepts Covered (and Where to Find Them)

| Concept                | Location                                                         |
|------------------------|------------------------------------------------------------------|
| **Abstraction**        | `Player.java` — `abstract class` with two abstract methods       |
| **Inheritance**        | `Batsman`, `Bowler`, `AllRounder` all `extends Player`           |
| **Polymorphism**       | `getPerformanceScore()` overridden in each subclass              |
| **Encapsulation**      | All fields private/protected with public getters/setters         |
| **`super` keyword**    | Each subclass constructor calls `super(...)`                     |
| **Custom Exception**   | `PlayerException extends Exception` (checked exception)          |
| **try-catch / throws** | `FileHandler.savePlayer()`, `IPLPlayerApp.handleSave()`          |
| **File Handling**      | `FileHandler.java` — `BufferedReader` / `BufferedWriter`         |
| **Static Methods**     | `FileHandler.savePlayer()`, `Batsman.fromString()` (factory)     |
| **Packages**           | `ipl.model`, `ipl.util`, `ipl.gui`                               |
| **Access Modifiers**   | `public`, `private`, `protected` used throughout                 |
| **Swing GUI**          | `JFrame`, `JTabbedPane`, `JTable`, `JComboBox`, `ActionListener` |
| **Custom Renderer**    | `DefaultTableCellRenderer` to highlight winner in gold           |

---

## 🧮 Scoring Formulas

**Batsman**
```
Score = (Average × 0.30) + (Strike Rate × 0.25) + (Runs/Match × 0.20)
      + (50s × 2 × 0.15) + (100s × 5 × 0.10)
```

**Bowler**
```
Score = (Wickets × 1.5 × 0.30) + ((12 - Economy) × 3 × 0.25)
      + (Wickets/Match × 10 × 0.25) + (4W × 0.10) + (5W × 0.10)
```

**All-Rounder** = average of the batting and bowling sub-scores.

---

## 🐛 Troubleshooting

| Symptom                                      | Fix                                                                                  |
|----------------------------------------------|--------------------------------------------------------------------------------------|
| `'javac' is not recognised`                  | Install the **JDK** (not just the JRE). Add `JAVA_HOME` to your PATH.                |
| VS Code shows *"Java extension not loaded"*  | Install the **Extension Pack for Java**, then reload the window.                     |
| Run/Debug lens missing                       | Open the project as a folder (not a single file) and let it import.                  |
| `error: package ipl.model does not exist`    | You compiled a single file — use `-sourcepath src` as in the command above.          |
| Swing fonts/emoji look square                | OS lacks the emoji font; the app still works — just looks plainer.                   |

---

## 🚀 Future Enhancements

- Live IPL stats integration via REST API
- Bar charts with JFreeChart for visual comparison
- JDBC + database persistence
- Wicketkeeper / Captain role-specific scoring
- Auction budget simulation
