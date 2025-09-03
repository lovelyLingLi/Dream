import { marked } from 'marked'
import Prism from 'prismjs'
import 'prismjs/themes/prism-tomorrow.css'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-css'

// Markdown解析配置
marked.setOptions({
    highlight(code, lang) {
        if (Prism.languages[lang]) {
            return Prism.highlight(code, Prism.languages[lang], lang)
        }
        return code
    },
    breaks: true,
    gfm: true
})

export const parseMarkdown = (content) => marked(content)
