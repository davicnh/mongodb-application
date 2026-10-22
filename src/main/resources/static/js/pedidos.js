const API_URL = "/pedidos";

document.addEventListener("DOMContentLoaded", () => {
    carregarPedidos();

    document.getElementById("btn-atualizar")
        .addEventListener("click", carregarPedidos);

    document.getElementById("btn-fechar-detalhes")
        .addEventListener("click", () => {
            document.getElementById("detalhes-pedido").hidden = true;
        });
});

async function carregarPedidos() {
    const tabela = document.getElementById("lista-pedidos");

    tabela.innerHTML = "<tr><td colspan='7'>Carregando pedidos...</td></tr>";
    mostrarMensagem("Carregando pedidos...", false);

    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível carregar os pedidos."));
        }

        const pedidos = await resposta.json();
        tabela.innerHTML = "";

        if (pedidos.length === 0) {
            tabela.innerHTML = "<tr><td colspan='7'>Nenhum pedido cadastrado.</td></tr>";
            mostrarMensagem("", false);
            return;
        }

        pedidos.forEach((pedido) => {
            const linha = document.createElement("tr");
            const id = pedido.id || "";
            const status = pedido.status || "";

            linha.innerHTML = `
                <td>${escaparHTML(id || "-")}</td>
                <td>${escaparHTML(obterNomeCliente(pedido))}</td>
                <td>${escaparHTML(formatarData(pedido.criadoEm))}</td>
                <td>${Array.isArray(pedido.itens) ? pedido.itens.length : 0}</td>
                <td>${escaparHTML(formatarMoeda(pedido.total))}</td>
                <td>${escaparHTML(status || "-")}</td>
                <td>
                    <button type="button" class="btn-detalhes">Detalhes</button>
                    <div class="acoes-status"></div>
                    <button type="button" class="btn-cancelar">Cancelar</button>
                </td>
            `;

            linha.querySelector(".btn-detalhes")
                .addEventListener("click", () => mostrarDetalhes(pedido));

            const areaStatus = linha.querySelector(".acoes-status");
            const proximoStatus = obterProximoStatus(status);

            if (proximoStatus && id) {
                const botaoStatus = document.createElement("button");
                botaoStatus.type = "button";
                botaoStatus.textContent = "Avançar para " + proximoStatus;
                botaoStatus.addEventListener("click", () =>
                    atualizarStatus(id, proximoStatus)
                );
                areaStatus.appendChild(botaoStatus);
            }

            const botaoCancelar = linha.querySelector(".btn-cancelar");

            if (status === "CANCELADO" || status === "ENTREGUE" || !id) {
                botaoCancelar.disabled = true;
            } else {
                botaoCancelar.addEventListener("click", () => cancelarPedido(id));
            }

            tabela.appendChild(linha);
        });

        mostrarMensagem("", false);
    } catch (erro) {
        tabela.innerHTML = "<tr><td colspan='7'>Não foi possível carregar os pedidos.</td></tr>";
        mostrarMensagem(erro.message, true);
    }
}

function obterProximoStatus(status) {
    const proximos = {
        CRIADO: "PAGO",
        PAGO: "ENVIADO",
        ENVIADO: "ENTREGUE"
    };

    return proximos[status] || null;
}

async function atualizarStatus(id, status) {
    if (!confirm(`Deseja avançar o pedido para ${status}?`)) {
        return;
    }

    try {
        const resposta = await fetch(
            `${API_URL}/${encodeURIComponent(id)}/status?status=${encodeURIComponent(status)}`,
            {method: "PUT"}
        );

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível atualizar o status."));
        }

        mostrarMensagem("Status atualizado com sucesso.", false);
        await carregarPedidos();
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

async function cancelarPedido(id) {
    if (!confirm("Tem certeza de que deseja cancelar este pedido?")) {
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/${encodeURIComponent(id)}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error(await lerErro(resposta, "Não foi possível cancelar o pedido."));
        }

        document.getElementById("detalhes-pedido").hidden = true;
        mostrarMensagem("Pedido cancelado. O estoque será atualizado pelo backend.", false);
        await carregarPedidos();
    } catch (erro) {
        mostrarMensagem(erro.message, true);
    }
}

function mostrarDetalhes(pedido) {
    document.getElementById("detalhe-id").textContent = pedido.id || "-";
    document.getElementById("detalhe-cliente").textContent = obterNomeCliente(pedido);
    document.getElementById("detalhe-status").textContent = pedido.status || "-";
    document.getElementById("detalhe-data").textContent = formatarData(pedido.criadoEm);
    document.getElementById("detalhe-total").textContent = formatarMoeda(pedido.total);

    const lista = document.getElementById("detalhe-itens");
    lista.innerHTML = "";

    if (Array.isArray(pedido.itens) && pedido.itens.length > 0) {
        pedido.itens.forEach((itemPedido) => {
            const item = document.createElement("li");

            item.textContent =
                `${itemPedido.nome || "Produto"} | ` +
                `SKU: ${itemPedido.sku || "-"} | ` +
                `Quantidade: ${itemPedido.quantidade} | ` +
                `Unitário: ${formatarMoeda(itemPedido.precoUnitario)} | ` +
                `Subtotal: ${formatarMoeda(itemPedido.subtotal)}`;

            lista.appendChild(item);
        });
    } else {
        lista.innerHTML = "<li>Nenhum item encontrado.</li>";
    }

    document.getElementById("detalhe-endereco").textContent =
        formatarEndereco(pedido.enderecoEntrega);

    const secao = document.getElementById("detalhes-pedido");
    secao.hidden = false;
    secao.scrollIntoView({behavior: "smooth", block: "start"});
}

function obterNomeCliente(pedido) {
    if (pedido.clienteSnapshot) {
        return pedido.clienteSnapshot.nome || pedido.clienteId || "-";
    }

    return pedido.clienteId || "-";
}

function formatarEndereco(endereco) {
    if (!endereco) {
        return "Endereço não informado.";
    }

    return [
        endereco.logradouro,
        endereco.numero,
        endereco.complemento,
        endereco.bairro,
        endereco.cidade,
        endereco.uf,
        endereco.cep
    ].filter((parte) => parte !== null && parte !== undefined && parte !== "")
        .join(", ") || "Endereço não informado.";
}

function formatarMoeda(valor) {
    return Number(valor ?? 0).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
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
