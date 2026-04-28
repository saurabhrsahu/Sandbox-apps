import { motion } from 'framer-motion'
import { ArrowRight, Sparkles } from 'lucide-react'

export function Hero() {
  return (
    <section id="story" className="relative pt-32 pb-20 sm:pt-40 sm:pb-28 overflow-hidden">
      <div className="pointer-events-none absolute -top-32 right-[-20%] h-[520px] w-[520px] rounded-full bg-gradient-to-br from-rose-200/80 via-amber-100/60 to-emerald-100/50 blur-3xl" />
      <div className="pointer-events-none absolute top-40 -left-32 h-72 w-72 rounded-full bg-gradient-to-tr from-sky-200/50 to-transparent blur-2xl" />

      <div className="relative mx-auto max-w-6xl px-4 sm:px-6">
        <motion.div
          initial={{ opacity: 0, y: 18 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.05, duration: 0.55 }}
          className="inline-flex items-center gap-2 rounded-full border border-bloom-foreground/10 bg-white/60 px-3 py-1.5 text-xs font-medium text-bloom-muted shadow-sm"
        >
          <Sparkles className="h-3.5 w-3.5 text-amber-600" aria-hidden />
          <span>Editorial-grade launch pages in one afternoon</span>
        </motion.div>

        <motion.h1
          initial={{ opacity: 0, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.12, duration: 0.6 }}
          className="display-serif mt-7 max-w-3xl text-balance text-4xl sm:text-5xl lg:text-6xl font-semibold leading-[1.08] tracking-tight text-bloom-foreground"
        >
          Tell the story before you ship the spreadsheet.
        </motion.h1>

        <motion.p
          initial={{ opacity: 0, y: 16 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.22, duration: 0.55 }}
          className="mt-6 max-w-xl text-lg text-bloom-muted leading-relaxed"
        >
          Bloom is a playground for warm gradients, confident typography, and motion that feels human — built with React
          so you can fork it into your next campaign or product slice.
        </motion.p>

        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.32, duration: 0.5 }}
          className="mt-10 flex flex-wrap items-center gap-4"
        >
          <a
            href="#waitlist"
            className="group inline-flex items-center gap-2 rounded-full bg-bloom-accent px-6 py-3 text-base font-semibold text-white shadow-lg shadow-rose-400/25 hover:brightness-105 transition-all"
          >
            Join the waitlist
            <ArrowRight className="h-4 w-4 transition-transform group-hover:translate-x-0.5" aria-hidden />
          </a>
          <a
            href="#craft"
            className="text-sm font-semibold text-bloom-foreground/80 underline decoration-bloom-foreground/25 underline-offset-4 hover:decoration-bloom-foreground/50"
          >
            See the craft
          </a>
        </motion.div>
      </div>
    </section>
  )
}
