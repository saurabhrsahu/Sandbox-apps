import { motion } from 'framer-motion'
import { Menu, X } from 'lucide-react'
import { useState } from 'react'
import { cn } from '@/lib/cn'

const links = [
  { href: '#story', label: 'Story' },
  { href: '#craft', label: 'Craft' },
  { href: '#waitlist', label: 'Waitlist' },
] as const

export function Navbar() {
  const [open, setOpen] = useState(false)

  return (
    <motion.header
      initial={{ y: -16, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.45, ease: [0.22, 1, 0.36, 1] }}
      className="fixed top-0 left-0 right-0 z-50 px-4 sm:px-6"
    >
      <div className="mx-auto max-w-6xl mt-4 rounded-2xl border border-bloom-foreground/10 bg-white/70 backdrop-blur-xl shadow-sm">
        <div className="flex items-center justify-between gap-4 px-4 py-3 sm:px-5">
          <a href="#" className="display-serif text-lg font-semibold tracking-tight text-bloom-foreground">
            Bloom
          </a>

          <nav className="hidden md:flex items-center gap-8 text-sm font-medium text-bloom-muted">
            {links.map((l) => (
              <a key={l.href} href={l.href} className="hover:text-bloom-foreground transition-colors">
                {l.label}
              </a>
            ))}
          </nav>

          <div className="hidden md:block">
            <a
              href="#waitlist"
              className="inline-flex items-center rounded-full bg-bloom-foreground px-4 py-2 text-sm font-semibold text-white shadow-md hover:opacity-95 transition-opacity"
            >
              Get early access
            </a>
          </div>

          <button
            type="button"
            className="md:hidden inline-flex h-10 w-10 items-center justify-center rounded-xl border border-bloom-foreground/10 text-bloom-foreground"
            aria-expanded={open}
            aria-label={open ? 'Close menu' : 'Open menu'}
            onClick={() => setOpen((v) => !v)}
          >
            {open ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </button>
        </div>

        <div
          className={cn(
            'md:hidden overflow-hidden transition-[max-height] duration-300 ease-out',
            open ? 'max-h-48 border-t border-bloom-foreground/10' : 'max-h-0',
          )}
        >
          <div className="flex flex-col gap-1 px-4 py-3">
            {links.map((l) => (
              <a
                key={l.href}
                href={l.href}
                className="rounded-lg px-3 py-2 text-sm font-medium text-bloom-muted hover:bg-bloom-foreground/5 hover:text-bloom-foreground"
                onClick={() => setOpen(false)}
              >
                {l.label}
              </a>
            ))}
            <a
              href="#waitlist"
              className="mt-1 rounded-full bg-bloom-foreground px-3 py-2.5 text-center text-sm font-semibold text-white"
              onClick={() => setOpen(false)}
            >
              Get early access
            </a>
          </div>
        </div>
      </div>
    </motion.header>
  )
}
