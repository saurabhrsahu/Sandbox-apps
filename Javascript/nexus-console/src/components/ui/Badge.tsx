import type { PropsWithChildren } from 'react'

import { cn } from '@/lib/cn'

type Variant = 'accent' | 'success' | 'warning' | 'danger' | 'muted'

export function Badge({
  className,
  variant = 'muted',
  children,
}: PropsWithChildren<{ className?: string; variant?: Variant }>) {
  return (
    <span
      className={cn(
        'inline-flex items-center rounded-full px-2.5 py-1 text-[11px] font-medium tracking-[0.04em]',
        variant === 'accent' &&
          'bg-[color-mix(in_oklch,var(--color-accent)_12%,transparent)] text-accent border border-accent/30',
        variant === 'success' &&
          'bg-[color-mix(in_oklch,var(--color-success)_12%,transparent)] text-success border border-success/35',
        variant === 'warning' &&
          'bg-[color-mix(in_oklch,var(--color-warning)_14%,transparent)] text-warning border border-warning/35',
        variant === 'danger' &&
          'bg-[color-mix(in_oklch,var(--color-danger)_13%,transparent)] text-danger border border-danger/35',
        variant === 'muted' && 'border border-border text-muted bg-surface/40',
        className,
      )}
    >
      {children}
    </span>
  )
}
