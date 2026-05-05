package ipl.gui;

import ipl.model.AllRounder;
import ipl.model.Batsman;
import ipl.model.Bowler;
import ipl.model.Player;
import ipl.util.FileHandler;
import ipl.util.PlayerException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main GUI application – IPL Player Comparison & Analysis Tool.
 * Demonstrates Java Swing: JFrame, JTabbedPane, JTable, JComboBox, ActionListener,
 * and a custom DefaultTableCellRenderer.
 */
public class IPLPlayerApp extends JFrame {

    private static final long serialVersionUID = 1L;

    private final List<Player> players = new ArrayList<>();

    // Tab 1 – Add Player components
    private JComboBox<String> roleCombo;
    private JTextField        idField, nameField, countryField, teamField, ageField, basePriceField;
    private JPanel            dynamicFieldsPanel;
    private final java.util.Map<String, JTextField> dynamicFields = new java.util.LinkedHashMap<>();

    // Tab 2 – Compare components
    private JComboBox<Player> playerA, playerB;
    private JPanel            comparisonResultPanel;

    // Tab 3 – All Players table
    private DefaultTableModel allPlayersModel;
    private JTable            allPlayersTable;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------
    public IPLPlayerApp() {
        super("🏏 IPL Player Comparison & Analysis Tool");

        // Load existing data
        players.addAll(FileHandler.loadPlayers());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("➕ Add Player",     buildAddPlayerTab());
        tabs.addTab("⚖ Compare Players", buildCompareTab());
        tabs.addTab("📋 All Players",    buildAllPlayersTab());

        add(tabs);
    }

    // ==================================================================
    // TAB 1 – ADD PLAYER
    // ==================================================================
    private JPanel buildAddPlayerTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Common fields ----
        JPanel common = new JPanel(new GridLayout(0, 2, 8, 8));
        common.setBorder(BorderFactory.createTitledBorder("Common Details"));

        roleCombo      = new JComboBox<>(new String[]{"Batsman", "Bowler", "All-Rounder"});
        idField        = new JTextField();
        nameField      = new JTextField();
        countryField   = new JTextField();
        teamField      = new JTextField();
        ageField       = new JTextField();
        basePriceField = new JTextField();

        common.add(new JLabel("Role:"));            common.add(roleCombo);
        common.add(new JLabel("Player ID:"));       common.add(idField);
        common.add(new JLabel("Name:"));            common.add(nameField);
        common.add(new JLabel("Country:"));         common.add(countryField);
        common.add(new JLabel("Team:"));            common.add(teamField);
        common.add(new JLabel("Age:"));             common.add(ageField);
        common.add(new JLabel("Base Price (Cr):")); common.add(basePriceField);

        // ---- Dynamic role-specific fields ----
        dynamicFieldsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        dynamicFieldsPanel.setBorder(BorderFactory.createTitledBorder("Role-Specific Stats"));
        renderDynamicFields("Batsman");

        roleCombo.addActionListener(e -> renderDynamicFields((String) roleCombo.getSelectedItem()));

        // ---- Buttons ----
        JButton saveBtn  = new JButton("💾 Save Player");
        JButton clearBtn = new JButton("🧹 Clear");

        saveBtn.addActionListener(e -> handleSave());
        clearBtn.addActionListener(e -> clearAddForm());

        JPanel buttons = new JPanel();
        buttons.add(saveBtn);
        buttons.add(clearBtn);

        // ---- Assemble ----
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.add(common);
        center.add(dynamicFieldsPanel);

        panel.add(new JLabel("<html><h2>Add a New IPL Player</h2></html>"), BorderLayout.NORTH);
        panel.add(center,  BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    /** Replace the dynamic-fields panel based on selected role. */
    private void renderDynamicFields(String role) {
        dynamicFieldsPanel.removeAll();
        dynamicFields.clear();

        String[] labels;
        switch (role) {
            case "Bowler":
                labels = new String[]{"Matches", "Wickets", "Economy",
                        "Bowling Avg", "Strike Rate", "4-Wkt Hauls", "5-Wkt Hauls"};
                break;
            case "All-Rounder":
                labels = new String[]{"Matches", "Runs", "Bat Avg", "Bat SR",
                        "50s", "100s", "Wickets", "Economy",
                        "Bowling Avg", "4-Wkt Hauls", "5-Wkt Hauls"};
                break;
            default: // Batsman
                labels = new String[]{"Matches", "Runs", "Average", "Strike Rate",
                        "50s", "100s", "Sixes", "Fours"};
        }
        for (String lbl : labels) {
            JTextField tf = new JTextField();
            dynamicFields.put(lbl, tf);
            dynamicFieldsPanel.add(new JLabel(lbl + ":"));
            dynamicFieldsPanel.add(tf);
        }
        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    /** Save button handler – validates input, builds Player, persists, refreshes UI. */
    private void handleSave() {
        try {
            String role = (String) roleCombo.getSelectedItem();
            String id   = required("Player ID", idField.getText());
            String name = required("Name",      nameField.getText());
            String cty  = required("Country",   countryField.getText());
            String team = required("Team",      teamField.getText());
            int    age  = parseInt("Age",       ageField.getText());
            double bp   = parseDouble("Base Price", basePriceField.getText());

            // Duplicate-ID check
            for (Player p : players) {
                if (p.getPlayerId().equalsIgnoreCase(id)) {
                    throw new PlayerException("Player ID '" + id + "' already exists.");
                }
            }

            Player newPlayer;
            switch (role) {
                case "Bowler":
                    newPlayer = new Bowler(id, name, cty, team, age, bp,
                            parseInt   ("Matches",    dyn("Matches")),
                            parseInt   ("Wickets",    dyn("Wickets")),
                            parseDouble("Economy",    dyn("Economy")),
                            parseDouble("Bowling Avg",dyn("Bowling Avg")),
                            parseDouble("Strike Rate",dyn("Strike Rate")),
                            parseInt   ("4-Wkt Hauls",dyn("4-Wkt Hauls")),
                            parseInt   ("5-Wkt Hauls",dyn("5-Wkt Hauls")));
                    break;
                case "All-Rounder":
                    newPlayer = new AllRounder(id, name, cty, team, age, bp,
                            parseInt   ("Matches",    dyn("Matches")),
                            parseInt   ("Runs",       dyn("Runs")),
                            parseDouble("Bat Avg",    dyn("Bat Avg")),
                            parseDouble("Bat SR",     dyn("Bat SR")),
                            parseInt   ("50s",        dyn("50s")),
                            parseInt   ("100s",       dyn("100s")),
                            parseInt   ("Wickets",    dyn("Wickets")),
                            parseDouble("Economy",    dyn("Economy")),
                            parseDouble("Bowling Avg",dyn("Bowling Avg")),
                            parseInt   ("4-Wkt Hauls",dyn("4-Wkt Hauls")),
                            parseInt   ("5-Wkt Hauls",dyn("5-Wkt Hauls")));
                    break;
                default: // Batsman
                    newPlayer = new Batsman(id, name, cty, team, age, bp,
                            parseInt   ("Matches",     dyn("Matches")),
                            parseInt   ("Runs",        dyn("Runs")),
                            parseDouble("Average",     dyn("Average")),
                            parseDouble("Strike Rate", dyn("Strike Rate")),
                            parseInt   ("50s",         dyn("50s")),
                            parseInt   ("100s",        dyn("100s")),
                            parseInt   ("Sixes",       dyn("Sixes")),
                            parseInt   ("Fours",       dyn("Fours")));
            }

            FileHandler.savePlayer(newPlayer);
            players.add(newPlayer);
            refreshAllPlayersTable();
            refreshComparisonDropdowns();

            JOptionPane.showMessageDialog(this,
                    "✅ " + newPlayer.getName() + " saved successfully!\n" +
                    "Performance Score: " + newPlayer.getPerformanceScore(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearAddForm();
        } catch (PlayerException ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ " + ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ Unexpected error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String dyn(String key) {
        JTextField f = dynamicFields.get(key);
        return f == null ? "" : f.getText();
    }

    private void clearAddForm() {
        idField.setText(""); nameField.setText(""); countryField.setText("");
        teamField.setText(""); ageField.setText(""); basePriceField.setText("");
        for (JTextField f : dynamicFields.values()) f.setText("");
    }

    // ------------------------------------------------------------------
    // Validation helpers (throw the custom checked exception)
    // ------------------------------------------------------------------
    private String required(String label, String value) throws PlayerException {
        if (value == null || value.trim().isEmpty()) {
            throw new PlayerException(label + " cannot be empty.");
        }
        return value.trim();
    }

    private int parseInt(String label, String value) throws PlayerException {
        try {
            return Integer.parseInt(required(label, value));
        } catch (NumberFormatException e) {
            throw new PlayerException(label + " must be a valid integer.");
        }
    }

    private double parseDouble(String label, String value) throws PlayerException {
        try {
            return Double.parseDouble(required(label, value));
        } catch (NumberFormatException e) {
            throw new PlayerException(label + " must be a valid number.");
        }
    }

    // ==================================================================
    // TAB 2 – COMPARE PLAYERS
    // ==================================================================
    private JPanel buildCompareTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        playerA = new JComboBox<>();
        playerB = new JComboBox<>();
        refreshComparisonDropdowns();

        JButton compareBtn = new JButton("⚖ Compare");
        compareBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { renderComparison(); }
        });

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Player A:")); top.add(playerA);
        top.add(new JLabel("Player B:")); top.add(playerB);
        top.add(compareBtn);

        comparisonResultPanel = new JPanel(new BorderLayout());
        comparisonResultPanel.add(new JLabel(
                "<html><i>Select two players above and click Compare.</i></html>",
                SwingConstants.CENTER), BorderLayout.CENTER);

        panel.add(new JLabel("<html><h2>Compare Two Players Side-by-Side</h2></html>"),
                BorderLayout.NORTH);
        panel.add(top, BorderLayout.CENTER);
        panel.add(comparisonResultPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshComparisonDropdowns() {
        if (playerA == null || playerB == null) return;
        playerA.removeAllItems();
        playerB.removeAllItems();
        for (Player p : players) {
            playerA.addItem(p);
            playerB.addItem(p);
        }
    }

    private void renderComparison() {
        Player a = (Player) playerA.getSelectedItem();
        Player b = (Player) playerB.getSelectedItem();
        comparisonResultPanel.removeAll();

        if (a == null || b == null) {
            comparisonResultPanel.add(new JLabel(
                    "Need at least two players to compare.", SwingConstants.CENTER));
        } else if (a == b) {
            comparisonResultPanel.add(new JLabel(
                    "Please pick two different players.", SwingConstants.CENTER));
        } else {
            String[] cols = {"Stat", a.getName(), b.getName()};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            model.addRow(new Object[]{"Role",       a.getRole(),    b.getRole()});
            model.addRow(new Object[]{"Country",    a.getCountry(), b.getCountry()});
            model.addRow(new Object[]{"Team",       a.getTeam(),    b.getTeam()});
            model.addRow(new Object[]{"Age",        a.getAge(),     b.getAge()});
            model.addRow(new Object[]{"Base Price", a.getBasePrice() + " Cr",
                                                    b.getBasePrice() + " Cr"});

            double scoreA = a.getPerformanceScore();
            double scoreB = b.getPerformanceScore();
            model.addRow(new Object[]{"⭐ Performance Score", scoreA, scoreB});

            JTable table = new JTable(model);
            table.setRowHeight(26);
            table.setFont(new Font("SansSerif", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

            // Highlight the winning player's column in gold (custom renderer)
            final int winnerCol = (scoreA >= scoreB) ? 1 : 2;
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                    if (col == winnerCol) {
                        c.setBackground(new Color(255, 215, 0));   // gold
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                    return c;
                }
            };
            for (int i = 1; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            String winnerName = (scoreA >= scoreB) ? a.getName() : b.getName();
            double winnerScore = Math.max(scoreA, scoreB);
            double loserScore  = Math.min(scoreA, scoreB);

            JLabel verdict = new JLabel(
                    "<html><div style='text-align:center;'><h3>🏆 " + winnerName +
                    " is the BETTER player with a Performance Score of " +
                    winnerScore + " vs " + loserScore + "</h3></div></html>",
                    SwingConstants.CENTER);
            verdict.setForeground(new Color(180, 120, 0));

            JPanel result = new JPanel(new BorderLayout(5, 10));
            result.add(new JScrollPane(table), BorderLayout.CENTER);
            result.add(verdict, BorderLayout.SOUTH);
            comparisonResultPanel.setPreferredSize(new Dimension(950, 350));
            comparisonResultPanel.add(result, BorderLayout.CENTER);
        }
        comparisonResultPanel.revalidate();
        comparisonResultPanel.repaint();
    }

    // ==================================================================
    // TAB 3 – ALL PLAYERS
    // ==================================================================
    private JPanel buildAllPlayersTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Name", "Role", "Country", "Team", "Age", "Base Price", "Score"};
        allPlayersModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        allPlayersTable = new JTable(allPlayersModel);
        allPlayersTable.setRowHeight(24);
        refreshAllPlayersTable();

        JButton deleteBtn  = new JButton("🗑 Delete Selected");
        JButton refreshBtn = new JButton("🔄 Refresh");

        deleteBtn.addActionListener(e -> handleDelete());
        refreshBtn.addActionListener(e -> {
            players.clear();
            players.addAll(FileHandler.loadPlayers());
            refreshAllPlayersTable();
            refreshComparisonDropdowns();
        });

        JPanel buttons = new JPanel();
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);

        panel.add(new JLabel("<html><h2>All Saved Players</h2></html>"), BorderLayout.NORTH);
        panel.add(new JScrollPane(allPlayersTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAllPlayersTable() {
        if (allPlayersModel == null) return;
        allPlayersModel.setRowCount(0);
        for (Player p : players) {
            allPlayersModel.addRow(new Object[]{
                    p.getPlayerId(), p.getName(), p.getRole(),
                    p.getCountry(), p.getTeam(), p.getAge(),
                    p.getBasePrice() + " Cr",
                    p.getPerformanceScore()
            });
        }
    }

    private void handleDelete() {
        int row = allPlayersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        String id   = (String) allPlayersModel.getValueAt(row, 0);
        String name = (String) allPlayersModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete player '" + name + "' (ID: " + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        players.removeIf(p -> p.getPlayerId().equals(id));
        try {
            FileHandler.saveAll(players);
            refreshAllPlayersTable();
            refreshComparisonDropdowns();
            JOptionPane.showMessageDialog(this, "Deleted '" + name + "'.");
        } catch (PlayerException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================================================================
    // MAIN ENTRY POINT
    // ==================================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { /* fall back to default */ }

        SwingUtilities.invokeLater(() -> new IPLPlayerApp().setVisible(true));
    }
}
