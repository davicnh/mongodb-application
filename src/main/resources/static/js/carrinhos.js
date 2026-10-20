const API_URL = "/carrinhosessao";

document.addEventListener("DOMContentLoaded", () => {
    carregarCarrinhos();

    document.getElementById("btn-atualizar")
        .addEventListener("click", carregarCarrinhos);

    document.getElementById("btn-fechar-detalhes")
        .addEventListener("click", () => {
            document.getElementById("detalhes-carrinho").hidden = true;
        });
});

async function carregarCarrinhos() {
    const tabela = document.getElementById("lista-carrinhos");

    try {
        mostrarMensagem("Carregando carrinhos...", false);

        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar os carrinhos.");
        }

        const carrinhos = await resposta.json();

        tabela.innerHTML = "";

        if (carrinhos.length === 0) {
            tabela.innerHTML = `
                <tr>
                    <td colspan="7">Nenhum carrinho encontrado.</td>
                </tr>
            `;

            mostrarMensagem("", false);
            return;
        }

        carrinhos.forEach((carrinho) => {
            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${escaparHTML(carrinho.id || "-")}</td>
                <td>${escaparHTML(carrinho.clienteId || "-")}</td>
                <td>${escaparHTML(carrinho.sessaoId || "-")}</td>
                <td>${Array.isArray(carrinho.items) ? carrinho.items.length : 0}</td>
                <td>${escaparHTML(formatarData(carrinho.atualizadoEm))}</td>
                <td>${escaparHTML(formatarData(carrinho.expiraEm))}</td>
                <td>
                    <button type="button" class="btn-detalhes">
                        Detalhes
                    </button>
                </td>
            `;

            linha.querySelector(".btn-detalhes").addEventListener(
                "click",
                () => mostrarDetalhes(carrinho)
            );

            tabela.appendChild(linha);
        });

        mostrarMensagem("", false);
    } catch (erro) {
        console.error("Erro ao carregar carrinhos:", erro);

        tabela.innerHTML = `
            <tr>
                <td colspan="7">
                    Não foi possível carregar os carrinhos.
                    Verifique se a API está funcionando.
                </td>
            </tr>
        `;

        mostrarMensagem(erro.message, true);
    }
}


function mostrarDetalhes(carrinho) {
    document.getElementById("detalhe-id").textContent =
        carrinho.id || "-";

    document.getElementById("detalhe-cliente").textContent =
        carrinho.clienteId || "-";

    document.getElementById("detalhe-sessao").textContent =
        carrinho.sessaoId || "-";

    document.getElementById("detalhe-atualizado").textContent =
        formatarData(carrinho.atualizadoEm);

    document.getElementById("detalhe-expira").textContent =
        formatarData(carrinho.expiraEm);

    const listaItens = document.getElementById("detalhe-itens");
    listaItens.innerHTML = "";

    if (Array.isArray(carrinho.items) && carrinho.items.length > 0) {
        carrinho.items.forEach((itemCarrinho) => {
            const item = document.createElement("li");

            item.textContent =
                `ID do produto: ${itemCarrinho.prudutoid || "-"} | ` +
                `Quantidade: ${itemCarrinho.quantidade}`;

            listaItens.appendChild(item);
        });
    } else {
        listaItens.innerHTML = "<li>Este carrinho não possui itens.</li>";
    }

    const secao = document.getElementById("detalhes-carrinho");
    secao.hidden = false;

    secao.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}


function formatarData(data) {
    if (!data) {
        return "-";
    }

    const dataFormatada = new Date(data);

    if (Number.isNaN(dataFormatada.getTime())) {
        return String(data);
    }

    return dataFormatada.toLocaleString("pt-BR");
}

function mostrarMensagem(texto, erro) {
    const mensagem = document.getElementById("mensagem");

    mensagem.textContent = texto;
    mensagem.style.color = erro ? "red" : "";
}

function escaparHTML(valor) {
    return String(valor)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
