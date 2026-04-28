import { motion } from 'framer-motion'
import { useState } from 'react'

export function CTABand() {
  const [sent, setSent] = useState(false)

  return (
    <section id="waitlist" className="py-14 sm:py-20 px-4 sm:px-6">
      <motion.div
        initial={{ opacity: 0, scale: 0.98 }}
        whileInView={{ opacity: 1, scale: 1 }}
        viewport={{ once: true }}
        transition={{ duration: 0.5 }}
        className="mx-auto max-w-4xl overflow-hidden rounded-[2rem] border border-white/60 bg-gradient-to-br from-emerald-800 via-emerald-900 to-neutral-950 p-px shadow-2xl shadow-emerald-900/35"
      >
        <div className="rounded-[calc(2rem-1px)] bg-gradient-to-br from-neutral-950/95 via-emerald-950/90 to-neutral-950 px-6 py-12 sm:p-14 text-center relative">
          <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_30%_-20%,rgba(251,232,217,0.15),transparent_52%)]" />
          <div className="relative">
            <h2 className="display-serif text-3xl sm:text-4xl font-semibold tracking-tight text-white text-balance">
              Early access closes quietly.
            </h2>
            <p className="mt-3 text-emerald-100/85 max-w-lg mx-auto text-base leading-relaxed">
              Leave your email. We reply with themes, typography tokens, and a deploy checklist — no funnel spam.
            </p>

            <form
              className="mt-10 flex flex-col sm:flex-row items-stretch gap-3 max-w-md mx-auto"
              onSubmit={(e) => {
                e.preventDefault()
                setSent(true)
              }}
            >
              <label className="sr-only" htmlFor="email-input">
                Email
              </label>
              <input
                id="email-input"
                name="email"
                type="email"
                required
                placeholder="you@studio.com"
                className="flex-1 min-w-0 rounded-full border border-white/15 bg-white/5 px-5 py-3.5 text-white placeholder:text-white/40 outline-none focus:ring-2 focus:ring-amber-200/40 text-base"
              />
              <button
                type="submit"
                disabled={sent}
                className="rounded-full bg-amber-100 px-6 py-3.5 text-base font-semibold text-neutral-950 shadow-lg hover:bg-white transition-colors disabled:opacity-85"
              >
                {sent ? "You're on the list" : 'Notify me'}
              </button>
            </form>

            <p className="mt-5 text-[11px] text-emerald-200/65">
              Sandbox demo — no email is transmitted. Wire your API later.
            </p>
          </div>
        </div>
      </motion.div>
    </section>
  )
}
