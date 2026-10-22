const API_URL = "/carrinhosessao";

let carrinhoSelecionado = null;

document.addEventListener("DOMContentLoaded", () => {
    carregarCarrinhos();

    document.getElementById("btn-atualizar")
        .addEventListener("click", carregarCarrinhos);

    document.getElementById("btn-fechar-detalhes")
        .addEventListener("click", fecharDetalhes);

    document.getElementById("form-criar-carrinho")
        .addEventListener("submit", criarCarrinho);

    document.getElementById("form-adicionar-item")
        .addEventListener("submit", adicionarItem);

    document.getElementById("btn-finalizar")
        .addEventListener("click", finalizarCompra);

    document.getElementById("btn-apagar")
        .addEventListener("click", apagarCarrinho);
});

async function carregarCarrinhos() {
    const tabela = document.getElementById("lista-carrinhos");

    tabela.innerHTML = "<tr><td colspan='7'>Carregando carrinhos...</td></tr>";

    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível carregar os carrinhos."));
        }

        const carrinhos = await resposta.json();
        tabela.innerHTML = "";

        if (carrinhos.length === 0) {
            tabela.innerHTML = "<tr><td colspan='7'>Nenhum carrinho encontrado.</td></tr>";
            return;
        }

        carrinhos.forEach((carrinho) => {
            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${escaparHTML(carrinho.id || "-")}</td>
                <td>${escaparHTML(carrinho.clienteId || "-")}</td>
                <td>${escaparHTML(carrinho.sessaoId || "-")}</td>
                <td>${Array.isArray(carrinho.itens) ? carrinho.itens.length : 0}</td>
                <td>${escaparHTML(formatarData(carrinho.atualizadoEm))}</td>
                <td>${escaparHTML(formatarData(carrinho.expiraEm))}</td>
                <td><button type="button" class="btn-detalhes">Detalhes</button></td>
            `;

            linha.querySelector(".btn-detalhes")
                .addEventListener("click", () => mostrarDetalhes(carrinho));

            tabela.appendChild(linha);
        });
    } catch (erro) {
        tabela.innerHTML = "<tr><td colspan='7'>Não foi possível carregar os carrinhos.</td></tr>";
        mostrarMensagem(erro.message, true);
    }
}

async function criarCarrinho(evento) {
    evento.preventDefault();

    const clienteId = document.getElementById("cliente-id").value.trim();
    const sessaoId = document.getElementById("sessao-id").value.trim();

    if (!clienteId) {
        mostrarMensagem("Informe o ID do cliente.", true);
        return;
    }

    const carrinho = {clienteId};

    if (sessaoId) {
        carrinho.sessaoId = sessaoId;
    }

    try {
        const resposta = await fetch(API_URL, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(carrinho)
        });

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível criar o carrinho."));
        }

        const criado = await resposta.json();

        document.getElementById("form-criar-carrinho").reset();
        await carregarCarrinhos();

        mostrarMensagem(`Carrinho criado com sucesso. ID: ${criado.id}`, false);
        mostrarDetalhes(criado);
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

function mostrarDetalhes(carrinho) {
    carrinhoSelecionado = carrinho.id;

    document.getElementById("detalhe-id").textContent = carrinho.id || "-";
    document.getElementById("detalhe-cliente").textContent = carrinho.clienteId || "-";
    document.getElementById("detalhe-sessao").textContent = carrinho.sessaoId || "-";
    document.getElementById("detalhe-atualizado").textContent = formatarData(carrinho.atualizadoEm);
    document.getElementById("detalhe-expira").textContent = formatarData(carrinho.expiraEm);

    const lista = document.getElementById("detalhe-itens");
    lista.innerHTML = "";

    if (Array.isArray(carrinho.itens) && carrinho.itens.length > 0) {
        carrinho.itens.forEach((itemCarrinho) => {
            const item = document.createElement("li");

            item.textContent =
                `Produto: ${itemCarrinho.produtoId || "-"} | ` +
                `Quantidade: ${itemCarrinho.quantidade} `;

            const botaoRemover = document.createElement("button");
            botaoRemover.type = "button";
            botaoRemover.textContent = "Remover";
            botaoRemover.addEventListener("click", () =>
                removerItem(carrinho.id, itemCarrinho.produtoId)
            );

            item.appendChild(document.createTextNode(" "));
            item.appendChild(botaoRemover);
            lista.appendChild(item);
        });
    } else {
        lista.innerHTML = "<li>Este carrinho não possui itens.</li>";
    }

    document.getElementById("detalhes-carrinho").hidden = false;
    document.getElementById("detalhes-carrinho")
        .scrollIntoView({behavior: "smooth", block: "start"});
}

async function adicionarItem(evento) {
    evento.preventDefault();

    if (!carrinhoSelecionado) {
        mostrarMensagem("Selecione um carrinho primeiro.", true);
        return;
    }

    const produtoId = document.getElementById("produto-id").value.trim();
    const quantidade = Number(document.getElementById("quantidade").value);

    if (!produtoId || !Number.isInteger(quantidade) || quantidade < 1) {
        mostrarMensagem("Informe um ID de produto e uma quantidade válida.", true);
        return;
    }

    try {
        const resposta = await fetch(
            `${API_URL}/${encodeURIComponent(carrinhoSelecionado)}/itens`,
            {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({produtoId, quantidade})
            }
        );

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível adicionar o produto."));
        }

        const carrinhoAtualizado = await resposta.json();

        document.getElementById("form-adicionar-item").reset();
        document.getElementById("quantidade").value = 1;

        mostrarDetalhes(carrinhoAtualizado);
        await carregarCarrinhos();
        mostrarMensagem("Produto adicionado ao carrinho.", false);
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

async function removerItem(carrinhoId, produtoId) {
    if (!confirm("Deseja remover este produto do carrinho?")) {
        return;
    }

    try {
        const resposta = await fetch(
            `${API_URL}/${encodeURIComponent(carrinhoId)}/itens/${encodeURIComponent(produtoId)}`,
            {method: "DELETE"}
        );

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível remover o item."));
        }

        const carrinhoAtualizado = await resposta.json();

        mostrarDetalhes(carrinhoAtualizado);
        await carregarCarrinhos();
        mostrarMensagem("Item removido do carrinho.", false);
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

async function finalizarCompra() {
    if (!carrinhoSelecionado) {
        mostrarMensagem("Selecione um carrinho primeiro.", true);
        return;
    }

    if (!confirm("Deseja finalizar a compra deste carrinho?")) {
        return;
    }

    try {
        const resposta = await fetch(
            `${API_URL}/${encodeURIComponent(carrinhoSelecionado)}/finalizar`,
            {method: "POST"}
        );

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível finalizar a compra."));
        }

        const pedido = await resposta.json();

        carrinhoSelecionado = null;
        fecharDetalhes();
        await carregarCarrinhos();

        mostrarMensagem(
            `Compra finalizada! Pedido criado: ${pedido.id}. Acesse a página de Pedidos para consultá-lo.`,
            false
        );
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

async function apagarCarrinho() {
    if (!carrinhoSelecionado) {
        mostrarMensagem("Selecione um carrinho primeiro.", true);
        return;
    }

    if (!confirm("Tem certeza de que deseja excluir este carrinho?")) {
        return;
    }

    try {
        const resposta = await fetch(
            `${API_URL}/${encodeURIComponent(carrinhoSelecionado)}`,
            {method: "DELETE"}
        );

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível excluir o carrinho."));
        }

        carrinhoSelecionado = null;
        fecharDetalhes();
        await carregarCarrinhos();
        mostrarMensagem("Carrinho excluído.", false);
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

function fecharDetalhes() {
    document.getElementById("detalhes-carrinho").hidden = true;
    carrinhoSelecionado = null;
}

function formatarData(data) {
    if (!data) return "-";

    const dataFormatada = new Date(data);

    return Number.isNaN(dataFormatada.getTime())
        ? String(data)
        : dataFormatada.toLocaleString("pt-BR");
}

async function lerErro(resposta, mensagemPadrao) {
    const texto = await resposta.text();
    return texto || mensagemPadrao;
}

function mostrarMensagem(texto, erro) {
    const mensagem = document.getElementById("mensagem");
    mensagem.textContent = texto;
    mensagem.style.color = erro ? "red" : "";
}

function escaparHTML(valor) {
    return String(valor ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
