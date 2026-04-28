import { Bento } from '@/components/Bento'
import { CTABand } from '@/components/CTABand'
import { Footer } from '@/components/Footer'
import { Hero } from '@/components/Hero'
import { MarqueeStrip } from '@/components/MarqueeStrip'
import { Navbar } from '@/components/Navbar'

export default function App() {
  return (
    <div className="relative min-h-svh">
      <Navbar />
      <Hero />
      <MarqueeStrip />
      <Bento />
      <CTABand />
      <Footer />
    </div>
  )
}
