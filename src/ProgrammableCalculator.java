import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class ProgrammableCalculator extends JFrame {
	private JTextField displayField;
	private JTextArea historyArea;
	private JTextArea programArea;
	private JLabel statusLabel;
	private double result = 0;
	private String expression = "";
	private boolean newInput = true;
	private final Map<String, Double> variables = new LinkedHashMap<>();
	private static final Color BG_DARK = new Color(45,10,30);
	private static final Color BG_PANEL = new Color(65,20,45);
	private static final Color BG_DISPLAY = new Color(30,5,20);
	private static final Color ACCENT = new Color(255,105,180);
	private static final Color ACCENT2 = new Color(255,20,147);
	private static final Color BTN_NUM = new Color(120,40,80);
	private static final Color BTN_OP = new Color(180,60,110);
	private static final Color BTN_FUNC = new Color(90,30,60);
	private static final Color BTN_PROG = new Color(150,50,100);
	private static final Color TEXT_BRIGHT = new Color(255,220,235);
	private static final Color TEXT_DIM = new Color(220,150,180);
	private static final Color HISTORY_BG = new Color(25,5,18);
	
	public ProgrammableCalculator() {
		buildUI();
		setTitle("Programmable Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void buildUI() {
		getContentPane().setBackground(BG_DARK);
		setLayout(new BorderLayout(8,8));
		((JPanel)getContentPane()).setBorder(new EmptyBorder(12,12,12,12));
		add(buildTitle(),            BorderLayout.NORTH);
		add(buildCenter(),            BorderLayout.CENTER);
		add(buildStatusBar(),            BorderLayout.SOUTH);
	}
	private JLabel buildTitle() {
		JLabel lbl = new JLabel("â¬i PROGRAMMABLE CALCULATOR", SwingConstants.CENTER);
		lbl.setFont(new Font("Monospaced", Font.BOLD, 15));
		lbl.setForeground(ACCENT);
		lbl.setBorder(new EmptyBorder(0,0,6,0));
		return lbl;
		}
	
	private JPanel buildCenter() {
		JPanel p = new JPanel(new BorderLayout(8,8));
		p.setBackground(BG_DARK);
		
		JPanel left = new JPanel(new BorderLayout(6,6));
		left.setBackground(BG_DARK);
		left.add(buildDisplay(), BorderLayout.NORTH);
		left.add(buildButtons(), BorderLayout.CENTER);
		
		JPanel right = new JPanel(new BorderLayout(6,6));
		right.setBackground(BG_DARK);
		right.setPreferredSize(new Dimension(220,0));
		right.add(buildHistory(), BorderLayout.CENTER);
		right.add(buildProgramPanel(), BorderLayout.SOUTH);
		
		p.add(left, BorderLayout.CENTER);
		p.add(right, BorderLayout.EAST);
		return p;
	}
	
	private JPanel buildDisplay() {
		JPanel p = new JPanel(new BorderLayout(4,4));
		p.setBackground(BG_DISPLAY);
		p.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(ACCENT, 1, true),
				new EmptyBorder(8,12,8,12)
				));
		displayField = new JTextField("0");
        displayField.setFont(new Font("Monospaced", Font.BOLD, 28));
        displayField.setForeground(TEXT_BRIGHT);
        displayField.setBackground(BG_DISPLAY);
        displayField.setBorder(null);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        p.add(displayField, BorderLayout.CENTER);
        return p;
    }
	
	private JPanel buildButtons() {
        JPanel p = new JPanel(new GridLayout(7, 5, 5, 5));
        p.setBackground(BG_DARK);
        addBtn(p, "sin",  BTN_FUNC, ACCENT,  () -> applyFunc("sin"));
        addBtn(p, "cos",  BTN_FUNC, ACCENT,  () -> applyFunc("cos"));
        addBtn(p, "tan",  BTN_FUNC, ACCENT,  () -> applyFunc("tan"));
        addBtn(p, "log",  BTN_FUNC, ACCENT,  () -> applyFunc("log"));
        addBtn(p, "ln",   BTN_FUNC, ACCENT,  () -> applyFunc("ln"));
        addBtn(p, "sq", BTN_FUNC, ACCENT,  () -> applyFunc("sqrt"));
        addBtn(p, "x^2",  BTN_FUNC, ACCENT,  () -> applySquare());
        addBtn(p, "x^y",  BTN_FUNC, ACCENT,  () -> appendOp("^"));
        addBtn(p, "1/x",  BTN_FUNC, ACCENT,  () -> applyReciprocal());
        addBtn(p, "abs",  BTN_FUNC, ACCENT,  () -> applyFunc("abs"));
        addBtn(p, "PI",   BTN_FUNC, ACCENT2, () -> insertConstant(Math.PI));
        addBtn(p, "E",    BTN_FUNC, ACCENT2, () -> insertConstant(Math.E));
        addBtn(p, "STO",  BTN_FUNC, ACCENT2, () -> storeVariable());
        addBtn(p, "RCL",  BTN_FUNC, ACCENT2, () -> recallVariable());
        addBtn(p, "VAR", BTN_FUNC, ACCENT2, () -> showVariables());
        addBtn(p, "7", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("7"));
        addBtn(p, "8", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("8"));
        addBtn(p, "9", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("9"));
        addBtn(p, "/", BTN_OP,  ACCENT,      () -> appendOp("/"));
        addBtn(p, "CE",BTN_OP,  ACCENT2,     () -> clearEntry());
        addBtn(p, "4", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("4"));
        addBtn(p, "5", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("5"));
        addBtn(p, "6", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("6"));
        addBtn(p, "*", BTN_OP,  ACCENT,      () -> appendOp("*"));
        addBtn(p, "C", BTN_OP,  ACCENT2,     () -> clearAll());
        addBtn(p, "1", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("1"));
        addBtn(p, "2", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("2"));
        addBtn(p, "3", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("3"));
        addBtn(p, "-", BTN_OP,  ACCENT,      () -> appendOp("-"));
        addBtn(p, "(", BTN_OP,  TEXT_DIM,    () -> appendStr("("));
        addBtn(p, "0", BTN_NUM, TEXT_BRIGHT, () -> appendDigit("0"));
        addBtn(p, ".", BTN_NUM, TEXT_BRIGHT, () -> appendDecimal());
        addBtn(p, "+/-",BTN_OP, TEXT_DIM,    () -> negate());
        addBtn(p, "+", BTN_OP,  ACCENT,      () -> appendOp("+"));
        addBtn(p, ")", BTN_OP,  TEXT_DIM,    () -> appendStr(")"));
        JButton eqBtn = makeBtn("  =  ", ACCENT, TEXT_BRIGHT);
        eqBtn.setFont(new Font("Monospaced", Font.BOLD, 22));
        eqBtn.addActionListener(e -> evaluate());
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setBackground(BG_DARK);
        container.add(p,     BorderLayout.CENTER);
        container.add(eqBtn, BorderLayout.SOUTH);
        return container;
    }
	
	private JPanel buildHistory() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 90), 1, true),
            new EmptyBorder(6, 6, 6, 6)
        ));
        JLabel lbl = new JLabel("HISTORY", SwingConstants.LEFT);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(ACCENT);
        historyArea = new JTextArea();
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        historyArea.setForeground(TEXT_DIM);
        historyArea.setBackground(HISTORY_BG);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        JScrollPane sp = new JScrollPane(historyArea);
        sp.setBorder(null);
        sp.setPreferredSize(new Dimension(210, 180));
        
        JButton clearHist = makeBtn("Clear", new Color(40, 20, 30), ACCENT2);
        clearHist.setFont(new Font("Monospaced", Font.PLAIN, 10));
        clearHist.addActionListener(e -> historyArea.setText(""));
        p.add(lbl,       BorderLayout.NORTH);
        p.add(sp,        BorderLayout.CENTER);
        p.add(clearHist, BorderLayout.SOUTH);
        return p;
    }
	
	private JPanel buildProgramPanel() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 40, 100), 1, true),
            new EmptyBorder(6, 6, 6, 6)
        ));
        JLabel lbl = new JLabel("PROGRAM / FORMULA", SwingConstants.LEFT);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(ACCENT2);
        programArea = new JTextArea(4, 18);
        programArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        programArea.setForeground(new Color(180, 255, 180));
        programArea.setBackground(new Color(10, 20, 14));
        programArea.setCaretColor(Color.WHITE);
        programArea.setLineWrap(true);
        programArea.setText("// Example:\n// pow(2,10)+sqrt(144)");
        JScrollPane sp = new JScrollPane(programArea);
        sp.setBorder(null);
        JButton runBtn = makeBtn("RUN", BTN_PROG, new Color(100, 255, 150));
        runBtn.setFont(new Font("Monospaced", Font.BOLD, 11));
        runBtn.addActionListener(e -> runProgram());
        p.add(lbl,    BorderLayout.NORTH);
        p.add(sp,     BorderLayout.CENTER);
        p.add(runBtn, BorderLayout.SOUTH);
        return p;
    }
	
	private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        statusLabel = new JLabel("Ready  |  Works on all Java versions (8, 11, 17, 21+)");
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
        statusLabel.setForeground(TEXT_DIM);
        p.add(statusLabel, BorderLayout.WEST);
        return p;
    }
	
	private void addBtn(JPanel panel, String text, Color bg, Color fg, Runnable action) {
        JButton btn = makeBtn(text, bg, fg);
        btn.addActionListener(e -> action.run());
        panel.add(btn);
    }
	
	private JButton makeBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(58, 42));
        Color hover = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
	
	private void appendDigit(String d) {
        if (newInput) { expression = ""; newInput = false; }
        expression += d;
        displayField.setText(expression);
    }
 
    private void appendOp(String op) {
        if (expression.isEmpty()) expression = String.valueOf(result);
        expression += op;
        displayField.setText(expression);
        newInput = false;
    }
    
    private void appendStr(String s) {
        if (newInput) { expression = ""; newInput = false; }
        expression += s;
        displayField.setText(expression);
    }
 
    private void appendDecimal() {
        if (newInput) { expression = "0"; newInput = false; }
        String[] tokens = expression.split("[+\\-*/^() ]+");
        String last = tokens.length > 0 ? tokens[tokens.length - 1] : "";
        if (!last.contains(".")) expression += ".";
        displayField.setText(expression);
    }
 
    private void clearEntry() {
        expression = ""; displayField.setText("0"); newInput = true; setStatus("Entry cleared");
    }
    private void clearAll() {
        expression = ""; result = 0; displayField.setText("0"); newInput = true; setStatus("Cleared");
    }
 
    private void negate() {
        try {
            double val = -Double.parseDouble(displayField.getText().trim());
            expression = formatResult(val);
            displayField.setText(expression);
        } catch (NumberFormatException ex) {
            expression = "-(" + expression + ")";
            displayField.setText(expression);
        }
    }
    
    private void insertConstant(double val) {
        if (newInput) { expression = ""; newInput = false; }
        expression += formatResult(val);
        displayField.setText(expression);
    }
 
    private void evaluate() {
        if (expression == null || expression.trim().isEmpty()) return;
        try {
            double res = new ExprParser(expression, variables).parse();
            result = res;
            String resultStr = formatResult(result);
            addHistory(expression + " = " + resultStr);
            displayField.setText(resultStr);
            expression = resultStr;
            newInput = true;
            setStatus("= " + resultStr);
        } catch (Exception ex) {
            displayField.setText("Error");
            setStatus("Error: " + ex.getMessage());
            newInput = true;
        }
    }
    
    private void applyFunc(String func) {
        String val = (expression == null || expression.isEmpty()) ? String.valueOf(result) : expression;
        expression = func + "(" + val + ")";
        displayField.setText(expression);
        evaluate();
    }
 
    private void applySquare() {
        String val = (expression == null || expression.isEmpty()) ? String.valueOf(result) : expression;
        expression = "(" + val + ")^2";
        displayField.setText(expression);
        evaluate();
    }
    
    private void applyReciprocal() {
    	String val = (expression == null || expression.isEmpty()) ? String.valueOf(result) : expression;
    	expression="1/(" + val + ")";
    	displayField.setText(expression);
    	evaluate();
    	}
    
    private void storeVariable() {
        String name = JOptionPane.showInputDialog(this,
            "Store result in variable (single letter A-Z):", "Store Variable", JOptionPane.PLAIN_MESSAGE);
        if (name != null && name.matches("[A-Za-z]")) {
            name = name.toUpperCase();
            variables.put(name, result);
            setStatus("Stored " + formatResult(result) + " -> " + name);
            addHistory("STO " + name + " = " + formatResult(result));
        } else if (name != null) {
            setStatus("Invalid name. Use a single letter A-Z.");
        }
    }
    
    private void recallVariable() {
        String name = JOptionPane.showInputDialog(this,
            "Recall variable (single letter A-Z):", "Recall Variable", JOptionPane.PLAIN_MESSAGE);
        if (name != null && name.matches("[A-Za-z]")) {
            name = name.toUpperCase();
            if (variables.containsKey(name)) {
                double val = variables.get(name);
                expression = formatResult(val);
                displayField.setText(expression);
                newInput = false;
                setStatus("Recalled " + name + " = " + formatResult(val));
            } else {
                setStatus("Variable " + name + " not set.");
            }
        }
    }
    
    private void showVariables() {
        if (variables.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No variables stored.", "Variables", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        variables.forEach((k, v) -> sb.append(k).append(" = ").append(formatResult(v)).append("\n"));
        JOptionPane.showMessageDialog(this, sb.toString(), "Stored Variables", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void runProgram() {
        String code = programArea.getText().trim();
        code = code.replaceAll("(?m)^\\s*//.*$", "").trim();
        if (code.isEmpty()) { setStatus("Program is empty."); return; }
        try {
            double res = new ExprParser(code, variables).parse();
            result = res;
            String resultStr = formatResult(result);
            displayField.setText(resultStr);
            expression = resultStr;
            newInput = true;
            addHistory("[PROG] " + code + " -> " + resultStr);
            setStatus("Program result: " + resultStr);
        } catch (Exception ex) {
            displayField.setText("Prog Err");
            setStatus("Program error: " + ex.getMessage());
        }
    }
 
    private String formatResult(double val) {
        if (val == Math.floor(val) && !Double.isInfinite(val) && Math.abs(val) < 1e15)
            return String.valueOf((long) val);
        return String.format("%.8g", val);
    }
 
    private void addHistory(String entry) {
        historyArea.append(entry + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
    
    private void setStatus(String msg) { statusLabel.setText(msg); }
    static class ExprParser {
            private final String input;
            private final Map<String, Double> vars;
            private int pos = 0;
     
            ExprParser(String input, Map<String, Double> vars) {
                this.input = input.trim();
                this.vars  = vars;
            }
            double parse() {
                double result = parseExpr();
                skipSpaces();
                if (pos < input.length())
                    throw new RuntimeException("Unexpected character: '" + input.charAt(pos) + "'");
                return result;
            }
     
            private double parseExpr() {
                double val = parseTerm();
                skipSpaces();
                while (pos < input.length()) {
                    char c = input.charAt(pos);
                    if      (c == '+') { pos++; val += parseTerm(); }
                    else if (c == '-') { pos++; val -= parseTerm(); }
                    else break;
                    skipSpaces();
                }
                return val;
            }
            
            private double parseTerm() {
                double val = parsePower();
                skipSpaces();
                while (pos < input.length()) {
                    char c = input.charAt(pos);
                    if (c == '*') { pos++; val *= parsePower(); }
                    else if (c == '/') {
                        pos++;
                        double div = parsePower();
                        if (div == 0) throw new RuntimeException("Division by zero");
                        val /= div;
                    }
                    else break;
                    skipSpaces();
                }
                return val;
            }
            
            private double parsePower() {
                double base = parseUnary();
                skipSpaces();
                if (pos < input.length() && input.charAt(pos) == '^') {
                    pos++;
                    return Math.pow(base, parsePower());
                }
                return base;
            }
     
            private double parseUnary() {
                skipSpaces();
                if (pos < input.length() && input.charAt(pos) == '-') { pos++; return -parsePrimary(); }
                if (pos < input.length() && input.charAt(pos) == '+') { pos++; return parsePrimary(); }
                return parsePrimary();
            }
            
            private double parsePrimary() {
                skipSpaces();
                if (pos >= input.length()) throw new RuntimeException("Unexpected end of expression");
     
                if (input.charAt(pos) == '(') {
                    pos++;
                    double val = parseExpr();
                    skipSpaces();
                    if (pos < input.length() && input.charAt(pos) == ')') pos++;
                    else throw new RuntimeException("Missing closing ')'");
                    return val;
                }
                if (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.') {
                    return parseNumber();
                }
     
                if (Character.isLetter(input.charAt(pos))) {
                    String name = parseName();
                    skipSpaces();
                    if (pos < input.length() && input.charAt(pos) == '(') {
                        pos++;
                        double arg1 = parseExpr();
                        skipSpaces();
                        if ("pow".equalsIgnoreCase(name) && pos < input.length() && input.charAt(pos) == ',') {
                            pos++;
                            double arg2 = parseExpr();
                            skipSpaces();
                            if (pos < input.length() && input.charAt(pos) == ')') pos++;
                            return Math.pow(arg1, arg2);
                        }
                        if (pos < input.length() && input.charAt(pos) == ')') pos++;
                        return applyFunction(name, arg1);
                    }
                    if ("PI".equalsIgnoreCase(name)) return Math.PI;
                    if ("E".equalsIgnoreCase(name))  return Math.E;
                    String upper = name.toUpperCase();
                    if (vars.containsKey(upper)) return vars.get(upper);
                    throw new RuntimeException("Unknown identifier: " + name);
                }
     
                throw new RuntimeException("Unexpected token: '" + input.charAt(pos) + "'");
            }
            
            private double parseNumber() {
                int start = pos;
                while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) pos++;
                if (pos < input.length() && (input.charAt(pos) == 'e' || input.charAt(pos) == 'E')) {
                    pos++;
                    if (pos < input.length() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) pos++;
                    while (pos < input.length() && Character.isDigit(input.charAt(pos))) pos++;
                }
                return Double.parseDouble(input.substring(start, pos));
            }
     
            private String parseName() {
                int start = pos;
                while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) pos++;
                return input.substring(start, pos);
            }
            
            private double applyFunction(String name, double arg) {
                switch (name.toLowerCase()) {
                    case "sin":   return Math.sin(arg);
                    case "cos":   return Math.cos(arg);
                    case "tan":   return Math.tan(arg);
                    case "log":   return Math.log10(arg);
                    case "ln":    return Math.log(arg);
                    case "sqrt":  return Math.sqrt(arg);
                    case "abs":   return Math.abs(arg);
                    case "ceil":  return Math.ceil(arg);
                    case "floor": return Math.floor(arg);
                    case "round": return (double) Math.round(arg);
                    case "exp":   return Math.exp(arg);
                    default: throw new RuntimeException("Unknown function: " + name);
                }
            }
            
            private void skipSpaces() {
                while (pos < input.length() && input.charAt(pos) == ' ') pos++;
            }
        }
     
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
                catch (Exception ignored) {}
                new ProgrammableCalculator();
            });
        }
    }