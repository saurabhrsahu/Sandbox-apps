import { useQuery } from '@tanstack/react-query'
import { motion } from 'framer-motion'
import {
  Area,
  CartesianGrid,
  ComposedChart,
  Legend,
  Line,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { Activity, Layers, ShieldCheck } from 'lucide-react'
import { fetchOverview } from '@/data/api'
import { Badge } from '@/components/ui/Badge'
import { Surface } from '@/components/ui/Surface'

const containerVariants = {
  hidden: {},
  visible: {
    transition: {
      staggerChildren: 0.06,
    },
  },
}

const cardVariants = {
  hidden: { opacity: 0, y: 12 },
  visible: { opacity: 1, y: 0 },
}

export function Dashboard() {
  const { data, isPending, error } = useQuery({
    queryKey: ['overview'],
    queryFn: fetchOverview,
  })

  if (error) {
    return (
      <Surface>
        <p className="text-danger text-sm font-medium">Failed to load overview.</p>
      </Surface>
    )
  }

  const kpis = data?.kpis ?? []
  const throughput = data?.throughput ?? []

  return (
    <div className="space-y-8">
      <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
        <p className="text-xs uppercase tracking-[0.2em] text-muted font-medium">Operational snapshot</p>
        <h1 className="mt-1 text-balance text-3xl font-semibold tracking-tight">
          Confidence at web scale — live mesh synthesis
        </h1>
        <p className="mt-2 max-w-2xl text-sm text-muted leading-relaxed">
          Composite health across ingestion, tenancy isolation, progressive delivery lanes, and cost guardrails — without
          leaving the command surface.
        </p>
      </motion.div>

      <motion.div
        className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 lg:gap-5"
        variants={containerVariants}
        initial="hidden"
        animate="visible"
      >
        {isPending
          ? Array.from({ length: 4 }).map((_, idx) => (
              <Surface key={`sk-${idx}`} className="h-36 animate-pulse bg-surface-elevated/50" bleed />
            ))
          : kpis.map((item) => (
              <motion.div key={item.id} variants={cardVariants} transition={{ duration: 0.35 }}>
                <Surface className="h-full hover:shadow-[0_0_0_1px_color-mix(in_oklch,var(--color-accent)_28%,transparent),var(--shadow-glass)] transition-shadow duration-300">
                  <div className="flex items-start justify-between gap-3">
                    <div className="space-y-3">
                      <span className="text-xs uppercase tracking-[0.12em] text-muted font-medium">{item.label}</span>
                      <div className="text-4xl font-semibold tracking-tight tabular-nums">{item.value}</div>
                      <p className="text-xs text-muted">{item.hint}</p>
                    </div>
                    <Badge
                      variant={
                        item.trend === 'up'
                          ? 'success'
                          : item.trend === 'down'
                            ? 'accent'
                            : 'muted'
                      }
                    >
                      {item.delta}
                    </Badge>
                  </div>
                </Surface>
              </motion.div>
            ))}
      </motion.div>

      <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
        <Surface className="xl:col-span-2 min-h-[420px]">
          <div className="flex flex-wrap items-center justify-between gap-3 mb-6">
            <div>
              <h2 className="text-base font-semibold tracking-tight">Throughput & error budget</h2>
              <p className="text-xs text-muted mt-0.5">Synthetic vs real — trailing window</p>
            </div>
            <div className="flex items-center gap-2">
              <Badge variant="accent">Live</Badge>
              <Badge variant="muted">p99 overlay</Badge>
            </div>
          </div>
          <div className="h-80 w-full">
            {!isPending && throughput.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <ComposedChart data={throughput}>
                  <CartesianGrid strokeDasharray="3 12" opacity={0.16} stroke="var(--color-border)" />
                  <XAxis dataKey="t" stroke="currentColor" tick={{ fill: 'var(--color-muted)' }} fontSize={10} />
                  <YAxis
                    yAxisId="left"
                    stroke="currentColor"
                    tick={{ fill: 'var(--color-muted)' }}
                    tickLine={false}
                    width={40}
                  />
                  <Tooltip
                    contentStyle={{
                      borderRadius: 14,
                      border: '1px solid color-mix(in oklch, var(--color-border), transparent)',
                      background: 'color-mix(in oklch, var(--color-surface), transparent 14%)',
                    }}
                  />
                  <Legend />
                  <Area
                    yAxisId="left"
                    type="monotone"
                    dataKey="req"
                    name="Ingress (krps scaled)"
                    fill="color-mix(in oklch, var(--color-accent), transparent 70%)"
                    stroke="var(--color-accent)"
                    strokeWidth={2}
                    fillOpacity={0.55}
                  />
                  <Line
                    yAxisId="left"
                    type="step"
                    dot={false}
                    dataKey="p99"
                    name="Latency p99 (ms)"
                    stroke="oklch(72% 0.16 295)"
                    strokeWidth={2}
                  />
                  <Line
                    yAxisId="left"
                    type="monotone"
                    dot={false}
                    dataKey="err"
                    name="Error rate (basis pts)"
                    stroke="oklch(65% 0.22 29)"
                    strokeWidth={2}
                  />
                </ComposedChart>
              </ResponsiveContainer>
            ) : (
              <div className="h-full rounded-xl bg-gradient-to-br from-accent/15 via-transparent to-fuchsia-500/10 animate-pulse" />
            )}
          </div>
        </Surface>

        <Surface className="space-y-4">
          <h2 className="text-base font-semibold tracking-tight">Fleet posture</h2>
          <div className="space-y-4">
            {[
              {
                icon: ShieldCheck,
                title: 'Policy mesh',
                body: 'mTLS rotations complete in all cells; zero hotspots above criticality Tier-2.',
                tone: 'success' as const,
              },
              {
                icon: Layers,
                title: 'Workload isolation',
                body: 'Quotas synthesized per tenant lanes — noisy neighbors flagged for review.',
                tone: 'warning' as const,
              },
              {
                icon: Activity,
                title: 'Incident debt',
                body: '3 low-severity open items bridged behind feature flags.',
                tone: 'muted' as const,
              },
            ].map((row) => (
              <motion.div
                key={row.title}
                layout
                className="flex gap-4 rounded-xl border border-border bg-surface-elevated/40 p-4"
              >
                <div className="h-11 w-11 rounded-xl border border-border flex items-center justify-center bg-accent/10 text-accent shrink-0">
                  <row.icon className="h-5 w-5" />
                </div>
                <div className="min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-medium text-sm">{row.title}</span>
                    <Badge variant={row.tone}>Signal</Badge>
                  </div>
                  <p className="text-xs text-muted leading-relaxed">{row.body}</p>
                </div>
              </motion.div>
            ))}
          </div>
        </Surface>
      </div>
    </div>
  )
}
