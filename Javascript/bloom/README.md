# Bloom

A second **sandbox JavaScript app**: warm **editorial landing** experience (hero, logo strip, bento features, waitlist CTA) — the opposite of Nexus Console’s dense ops UI.

- **Vite 8** + **React 19** + **TypeScript**
- **Tailwind CSS v4** (`@tailwindcss/vite`) with custom Bloom tokens
- **Framer Motion** for scroll-aware and hero motion
- Fonts: **Fraunces** + **Source Sans 3** (via Google Fonts)

## Run

```powershell
cd JavaScript/bloom
npm install
npm run dev
```

Open the URL Vite prints (default `http://127.0.0.1:5173` unless port is taken).

```powershell
npm run build
npm run preview
```

The waitlist form only toggles local state (no network) — wire it to your API when you integrate.
