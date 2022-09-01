function y(n) {
    ["link", "go"].includes(n) && window.scrollTo({ top: 0 });
}
function d(n) {
    const e = new URL(n || window.location.href).href;
    return e.endsWith("/") || e.includes(".") ? e : `${e}/`;
}
function v(n) {
    (!window.history.state || window.history.state.url !== n) && window.history.pushState({ url: n }, "internalLink", n);
}
function E(n) {
    document.querySelector(n).scrollIntoView({ behavior: "smooth", block: "start" });
}
function k(n) {
    const e = d();
    return { type: "popstate", next: e };
}
function F(n) {
    let e;
    if (n.altKey || n.ctrlKey || n.metaKey || n.shiftKey)
        return { type: "disqualified" };
    for (let t = n.target; t.parentNode; t = t.parentNode)
        if (t.nodeName === "A") {
            e = t;
            break;
        }
    if (e && e.host !== location.host)
        return e.target = "_blank", { type: "external" };
    if (e && "cold" in (e == null ? void 0 : e.dataset))
        return { type: "disqualified" };
    if (e != null && e.hasAttribute("href")) {
        const t = e.getAttribute("href"), r = new URL(t, location.href);
        if (n.preventDefault(), t != null && t.startsWith("#"))
            return E(t), { type: "scrolled" };
        const o = d(r.href), i = d();
        return { type: "link", next: o, prev: i };
    } else
        return { type: "noop" };
}
function L(n) {
    return new DOMParser().parseFromString(n, "text/html");
}
function w(n) {
    document.body.replaceWith(n.body);
}
function N(n) {
    const e = (s) => Array.from(s.querySelectorAll('head>:not([rel="prefetch"]')), t = e(document), r = e(n), { staleNodes: o, freshNodes: i } = S(t, r);
    o.forEach((s) => s.remove()), document.head.append(...i);
}
function S(n, e) {
    const t = [], r = [];
    let o = 0, i = 0;
    for (; o < n.length || i < e.length; ) {
        const s = n[o], c = e[i];
        if (s != null && s.isEqualNode(c)) {
            o++, i++;
            continue;
        }
        const h = s ? r.findIndex((a) => a.isEqualNode(s)) : -1;
        if (h !== -1) {
            r.splice(h, 1), o++;
            continue;
        }
        const l = c ? t.findIndex((a) => a.isEqualNode(c)) : -1;
        if (l !== -1) {
            t.splice(l, 1), i++;
            continue;
        }
        s && t.push(s), c && r.push(c), o++, i++;
    }
    return { staleNodes: t, freshNodes: r };
}
function m() {
    document.head.querySelectorAll("[data-reload]").forEach(g), document.body.querySelectorAll("script").forEach(g);
}
function g(n) {
    const e = document.createElement("script"), t = Array.from(n.attributes);
    for (const { name: r, value: o } of t)
        e[r] = o;
    e.append(n.textContent), n.replaceWith(e);
}
const x = {
    log: !1,
    pageTransitions: !1
};
class A {
    constructor(e) {
        this.opts = e, this.enabled = !0, this.prefetched = /* @__PURE__ */ new Set(), this.opts = { ...x, ...e != null ? e : {} }, window != null && window.history ? (document.addEventListener("click", (t) => this.onClick(t)), window.addEventListener("popstate", (t) => this.onPop(t)), this.prefetch()) : (console.warn("flamethrower router not supported in this browser or environment"), this.enabled = !1);
    }
    go(e) {
        const t = window.location.href, r = new URL(e, location.origin).href;
        return this.reconstructDOM({ type: "go", next: r, prev: t });
    }
    back() {
        window.history.back();
    }
    forward() {
        window.history.forward();
    }
    get allLinks() {
        return Array.from(document.links).filter(
            (e) => e.href.includes(document.location.origin) && !e.href.includes("#") && e.href !== (document.location.href || document.location.href + "/") && !this.prefetched.has(e.href)
        );
    }
    log(...e) {
        this.opts.log && console.log(...e);
    }
    prefetch() {
        if (this.opts.prefetch === "visible")
            this.prefetchVisible();
        else if (this.opts.prefetch === "hover")
            this.prefetchOnHover();
        else
            return;
    }
    prefetchOnHover() {
        this.allLinks.forEach((e) => {
            const t = e.getAttribute("href");
            e.addEventListener("pointerenter", () => this.createLink(t), { once: !0 });
        });
    }
    prefetchVisible() {
        const e = {
            root: null,
            rootMargin: "0px",
            threshold: 1
        };
        "IntersectionObserver" in window && (this.observer || (this.observer = new IntersectionObserver((t, r) => {
            t.forEach((o) => {
                const i = o.target.getAttribute("href");
                if (this.prefetched.has(i)) {
                    r.unobserve(o.target);
                    return;
                }
                o.isIntersecting && (this.createLink(i), r.unobserve(o.target));
            });
        }, e)), this.allLinks.forEach((t) => this.observer.observe(t)));
    }
    createLink(e) {
        const t = document.createElement("link");
        t.rel = "prefetch", t.href = e, t.as = "document", t.onload = () => this.log("\u{1F329}\uFE0F prefetched", e), t.onerror = (r) => this.log("\u{1F915} can't prefetch", e, r), document.head.appendChild(t), this.prefetched.add(e);
    }
    onClick(e) {
        this.reconstructDOM(F(e));
    }
    onPop(e) {
        this.reconstructDOM(k());
    }
    async reconstructDOM({ type: e, next: t, prev: r }) {
        if (!this.enabled) {
            this.log("router disabled");
            return;
        }
        try {
            if (this.log("\u26A1", e), ["popstate", "link", "go"].includes(e) && t !== r) {
                this.opts.log && console.time("\u23F1\uFE0F"), window.dispatchEvent(new CustomEvent("flamethrower:router:fetch")), e != "popstate" && v(t);
                const i = await (await fetch(t, { headers: { "X-Flamethrower": "1" } }).then((c) => {
                    const h = c.body.getReader(), l = parseInt(c.headers.get("Content-Length"));
                    let a = 0;
                    return new ReadableStream({
                        start(u) {
                            function f() {
                                h.read().then(({ done: b, value: p }) => {
                                    if (b) {
                                        u.close();
                                        return;
                                    }
                                    a += p.length, window.dispatchEvent(
                                        new CustomEvent("flamethrower:router:fetch-progress", {
                                            detail: {
                                                progress: Number.isNaN(l) ? 0 : a / l * 100,
                                                received: a,
                                                length: l || 0
                                            }
                                        })
                                    ), u.enqueue(p), f();
                                });
                            }
                            f();
                        }
                    });
                }).then((c) => new Response(c, { headers: { "Content-Type": "text/html" } }))).text(), s = L(i);
                N(s), this.opts.pageTransitions && document.createDocumentTransition ? document.createDocumentTransition().start(() => {
                    w(s), m();
                }) : (w(s), m()), y(e), window.dispatchEvent(new CustomEvent("flamethrower:router:end")), setTimeout(() => {
                    this.prefetch();
                }, 200), this.opts.log && console.timeEnd("\u23F1\uFE0F");
            }
        } catch (o) {
            return window.dispatchEvent(new CustomEvent("flamethrower:router:error", o)), this.opts.log && console.timeEnd("\u23F1\uFE0F"), console.error("\u{1F4A5} router fetch failed", o), !1;
        }
    }
}
const C = (n) => {
    const e = new A(n);
    if (n.log && console.log("\u{1F525} flamethrower engaged"), window) {
        const t = window;
        t.flamethrower = e;
    }
    return e;
};
export {
    C as default
};