import type { PropsWithChildren } from 'react'
import { cn } from '@/lib/cn'

export function Surface({
  className,
  children,
  bleed = false,
}: PropsWithChildren<{ className?: string; bleed?: boolean }>) {
  return (
    <div
      className={cn(
        'rounded-2xl bg-surface glass-panel overflow-hidden shadow-[var(--shadow-glass)] text-foreground',
        bleed ? 'p-0' : 'p-5 sm:p-6',
        className,
      )}
    >
      {children}
    </div>
  )
}
