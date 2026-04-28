import { useEffect } from 'react'
import { useUI } from '@/stores/ui'

/** Maps theme mode + system preference to `.dark` on <html>. */
export function ThemeSync() {
  const theme = useUI((s) => s.theme)

  useEffect(() => {
    const root = document.documentElement

    const apply = () => {
      if (theme === 'dark') root.classList.add('dark')
      else if (theme === 'light') root.classList.remove('dark')
      else if (theme === 'system') {
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
        root.classList.toggle('dark', prefersDark)
      }
    }
    apply()

    if (theme !== 'system') return

    const mq = window.matchMedia('(prefers-color-scheme: dark)')
    const onChange = () => apply()
    mq.addEventListener('change', onChange)
    return () => mq.removeEventListener('change', onChange)
  }, [theme])

  return null
}
