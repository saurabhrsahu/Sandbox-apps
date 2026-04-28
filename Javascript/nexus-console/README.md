# Nexus Console

High-fidelity **React 19 + TypeScript + Vite** control-plane UI: glass layout, OKLCH theming, Framer Motion, **TanStack Query** with mocked APIs, **Recharts** dashboards, and sortable workload tables. Use it as a sandbox for front-end architecture, storybook-style polish, and CI builds.

## Stack

- **Vite 8** · **React 19** · **TypeScript 6**
- **Tailwind CSS v4** (`@tailwindcss/vite`) with custom `@theme` tokens
- **react-router-dom** · **zustand** (persisted UI) · **@tanstack/react-query**
- **framer-motion** · **recharts** · **lucide-react**

## Scripts

```powershell
cd JavaScript/nexus-console
npm install
npm run dev
```

Open the URL Vite prints (usually `http://127.0.0.1:5173`).

```powershell
npm run build
npm run preview
```

## Layout

- **Command** (`/`) — KPI grid, composed chart, fleet posture cards
- **Workloads** (`/workloads`) — regional shard table with sortable columns and saturation meters
- **Pipelines** (`/pipelines`) — multi-stage delivery lanes (mock)
- **Control** (`/settings`) — theme (system / light / dark) and notes

Data lives in `src/data/api.ts` (artificial latency). Replace those functions with real `fetch` calls to your API (for example PulseBoard) when integrating.

## Notes

- Theme preference is stored in `localStorage` under `nexus-console-ui`.
- If `npm run build` fails on missing `enhanced-resolve` files, remove `node_modules` and reinstall dependencies.
