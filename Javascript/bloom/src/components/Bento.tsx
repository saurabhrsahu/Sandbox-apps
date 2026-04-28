import { motion } from 'framer-motion'
import { Blocks, Palette, Shield } from 'lucide-react'

const cards = [
  {
    icon: Palette,
    title: 'Hue & harmony',
    desc: 'Rose, moss, and dusk tones — tuned so your hero never fights the body copy.',
    className: 'sm:col-span-2',
  },
  {
    icon: Shield,
    title: 'Trust primitives',
    desc: 'Sticky nav, microcopy, and accessibility defaults you can ship unchanged.',
    className: '',
  },
  {
    icon: Blocks,
    title: 'Composable blocks',
    desc: 'Hooks + Framer Motion — swap sections without fighting brittle markup.',
    className: '',
  },
] as const

export function Bento() {
  return (
    <section id="craft" className="py-24 sm:py-32">
      <div className="mx-auto max-w-6xl px-4 sm:px-6">
        <p className="text-xs uppercase tracking-[0.2em] text-bloom-muted font-semibold">The craft</p>
        <h2 className="display-serif mt-2 text-3xl sm:text-4xl font-semibold tracking-tight text-bloom-foreground text-balance max-w-xl">
          Everything opinionated enough to reuse, humble enough to override.
        </h2>

        <div className="mt-12 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {cards.map((c, i) => (
            <motion.article
              key={c.title}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true, margin: '-40px' }}
              transition={{ delay: i * 0.08, duration: 0.45 }}
              className={`rounded-3xl border border-bloom-foreground/10 bg-white/65 p-6 shadow-sm backdrop-blur-sm hover:shadow-md transition-shadow ${c.className}`}
            >
              <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-rose-50 to-emerald-50 text-bloom-accent border border-white">
                <c.icon className="h-5 w-5" />
              </div>
              <h3 className="mt-5 display-serif text-xl font-semibold text-bloom-foreground">{c.title}</h3>
              <p className="mt-2 text-sm text-bloom-muted leading-relaxed">{c.desc}</p>
            </motion.article>
          ))}
        </div>
      </div>
    </section>
  )
}
