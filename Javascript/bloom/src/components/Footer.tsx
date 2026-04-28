export function Footer() {
  const year = new Date().getFullYear()
  return (
    <footer className="border-t border-bloom-foreground/10 bg-white/40 backdrop-blur py-14">
      <div className="mx-auto flex max-w-6xl flex-col gap-10 px-4 sm:px-6 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <p className="display-serif text-xl font-semibold text-bloom-foreground">Bloom</p>
          <p className="mt-3 max-w-xs text-sm text-bloom-muted leading-relaxed">
            A sibling sandbox to richer dashboards — keep both under <code className="font-mono text-xs">JavaScript/</code>{' '}
            and remix freely.
          </p>
        </div>
        <div className="flex flex-wrap gap-8 text-sm">
          <div>
            <p className="font-semibold text-bloom-foreground">Navigate</p>
            <ul className="mt-3 space-y-2 text-bloom-muted">
              <li>
                <a className="hover:text-bloom-foreground transition-colors" href="#story">
                  Story
                </a>
              </li>
              <li>
                <a className="hover:text-bloom-foreground transition-colors" href="#craft">
                  Craft
                </a>
              </li>
            </ul>
          </div>
          <div>
            <p className="font-semibold text-bloom-foreground">Stack</p>
            <ul className="mt-3 space-y-2 text-bloom-muted">
              <li>React 19 + Vite</li>
              <li>Tailwind CSS v4</li>
              <li>Framer Motion</li>
            </ul>
          </div>
        </div>
      </div>
      <p className="mx-auto mt-12 max-w-6xl px-4 sm:px-6 text-center text-xs text-bloom-muted">
        © {year} Sandbox Apps — Bloom is for learning layouts and launches.
      </p>
    </footer>
  )
}
