# HighFi Java App - PulseBoard

PulseBoard is a polished Java Swing desktop application with a high-fidelity UI.

## Features

- Modern gradient header and card-based dashboard
- Sidebar navigation (Overview, Tasks, Insights)
- Interactive task list with live focus meter updates
- Modular architecture (`model`, `service`, `ui/components`, `ui/panels`)
- Lightweight, no external dependencies

## Requirements

- Java 17 or newer (Java 21 also works)
- `java` and `javac` available on your PATH

## Project Structure

```text
Java/
  src/com/highfi/dashboard/...   # source code
  out/...                        # compiled classes (generated)
  run.bat                        # build/run helper script
```

## How To Run

Open terminal in the `Java` folder.

### Option 1: Quick Run (recommended)

Build + launch app:

```powershell
run.bat
```

Build only:

```powershell
run.bat build
```

### Option 2: Manual Commands

```powershell
javac -d out -sourcepath src src\com\highfi\dashboard\HighFiDashboardApp.java
java -cp out com.highfi.dashboard.HighFiDashboardApp
```

## Troubleshooting

- If `java` is not recognized, install a JDK and restart terminal.
- If compile fails, run `run.bat build` and check the first error shown.
- If app does not open, ensure your system allows desktop GUI apps.
