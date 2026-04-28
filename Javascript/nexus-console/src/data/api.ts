const delay = (ms: number) =>
  new Promise<void>((resolve) => {
    window.setTimeout(resolve, ms)
  })

export type KpiBlock = {
  id: string
  label: string
  value: string
  delta: string
  trend: 'up' | 'down' | 'flat'
  hint: string
}

export type ThroughputPoint = {
  t: string
  req: number
  err: number
  p99: number
}

export type ShardRow = {
  id: string
  region: string
  role: 'primary' | 'replica' | 'staging'
  saturation: number
  queueDepth: number
  version: string
}

export type PipelineRun = {
  id: string
  name: string
  status: 'healthy' | 'degraded' | 'failed'
  startedAt: string
  stages: { name: string; state: 'done' | 'active' | 'pending' | 'failed' }[]
}

export async function fetchOverview() {
  await delay(320 + Math.random() * 260)
  const kpis: KpiBlock[] = [
    {
      id: 'ingress',
      label: 'Ingress RPS',
      value: '842K',
      delta: '+12.4%',
      trend: 'up',
      hint: 'vs 7d trailing average',
    },
    {
      id: 'p99',
      label: 'Edge p99 latency',
      value: '146ms',
      delta: '−8ms',
      trend: 'down',
      hint: 'weighted across regions',
    },
    {
      id: 'error',
      label: 'Synthetic error budget',
      value: '0.28%',
      delta: '+0.02%',
      trend: 'flat',
      hint: 'within SLO guardrails',
    },
    {
      id: 'cost',
      label: 'Blended unit cost',
      value: '$4.12',
      delta: '−3.1%',
      trend: 'down',
      hint: 'per million evaluated events',
    },
  ]

  const throughput: ThroughputPoint[] = Array.from({ length: 18 }, (_, i) => {
    const phase = i * 0.35
    return {
      t: `T-${18 - i}`,
      req: 420 + Math.sin(phase) * 90 + (i % 4) * 12,
      err: 4 + (i % 3) * 0.7 + Math.sin(phase * 1.2),
      p99: 120 + Math.cos(phase) * 34 + (i % 5) * 3,
    }
  })

  return { kpis, throughput }
}

export async function fetchShards() {
  await delay(260 + Math.random() * 200)
  const rows: ShardRow[] = [
    {
      id: 'us-east-1a',
      region: 'US East (N. Virginia)',
      role: 'primary',
      saturation: 0.62,
      queueDepth: 1284,
      version: 'rel-2026.04.18',
    },
    {
      id: 'eu-west-2b',
      region: 'EU West (London)',
      role: 'replica',
      saturation: 0.41,
      queueDepth: 412,
      version: 'rel-2026.04.18',
    },
    {
      id: 'ap-south-1a',
      region: 'AP South (Mumbai)',
      role: 'replica',
      saturation: 0.55,
      queueDepth: 901,
      version: 'rel-2026.04.16',
    },
    {
      id: 'staging-1',
      region: 'Global — staging cell',
      role: 'staging',
      saturation: 0.18,
      queueDepth: 24,
      version: 'beta-2026.04.20',
    },
  ]
  return rows
}

export async function fetchPipelines() {
  await delay(280 + Math.random() * 240)
  const runs: PipelineRun[] = [
    {
      id: 'deploy-prod',
      name: 'release / control-plane',
      status: 'healthy',
      startedAt: new Date(Date.now() - 12 * 60 * 1000).toISOString(),
      stages: [
        { name: 'build', state: 'done' },
        { name: 'integration', state: 'done' },
        { name: 'canary', state: 'active' },
        { name: 'promote', state: 'pending' },
      ],
    },
    {
      id: 'data-plane',
      name: 'data-plane / rolling migration',
      status: 'degraded',
      startedAt: new Date(Date.now() - 44 * 60 * 1000).toISOString(),
      stages: [
        { name: 'preflight', state: 'done' },
        { name: 'schema', state: 'done' },
        { name: 'backfill', state: 'active' },
        { name: 'cutover', state: 'pending' },
      ],
    },
    {
      id: 'security-scan',
      name: 'supply-chain attestations',
      status: 'failed',
      startedAt: new Date(Date.now() - 6 * 60 * 60 * 1000).toISOString(),
      stages: [
        { name: 'sbom', state: 'done' },
        { name: 'signatures', state: 'failed' },
        { name: 'policy', state: 'pending' },
      ],
    },
  ]
  return runs
}
