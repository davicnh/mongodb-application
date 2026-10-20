const API_URL = "/pedidos";

document.addEventListener("DOMContentLoaded", () => {
    carregarPedidos();

    const botaoAtualizar = document.getElementById("btn-atualizar");

    if (botaoAtualizar) {
        botaoAtualizar.addEventListener("click", carregarPedidos);
    }
});

async function carregarPedidos() {
    const tabela = document.getElementById("lista-pedidos");
    const mensagem = document.getElementById("mensagem");

    try {
        mostrarMensagem("Carregando pedidos...", false);

        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar os pedidos.");
        }

        const pedidos = await resposta.json();

        tabela.innerHTML = "";

        if (pedidos.length === 0) {
            tabela.innerHTML = `
                <tr>
                    <td colspan="6">Nenhum pedido cadastrado.</td>
                </tr>
            `;

            mostrarMensagem("", false);
            return;
        }

        pedidos.forEach((pedido) => {
            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${pedido.id || "-"}</td>
                <td>${obterNomeCliente(pedido)}</td>
                <td>${formatarData(pedido.criadoEm)}</td>
                <td>${pedido.itens ? pedido.itens.length : 0}</td>
                <td>${formatarMoeda(pedido.total)}</td>
                <td>${pedido.status || "-"}</td>
                <td>
                    <button type="button" class="btn-detalhes">
                        Detalhes
                    </button>
                </td>
            `;

            linha.querySelector(".btn-detalhes").addEventListener(
                "click",
                () => mostrarDetalhes(pedido)
            );

            tabela.appendChild(linha);
        });

        mostrarMensagem("", false);
    } catch (erro) {
        console.error("Erro ao carregar pedidos:", erro);

        tabela.innerHTML = `
            <tr>
                <td colspan="7">
                    Não foi possível carregar os pedidos.
                    Verifique se a API está funcionando.
                </td>
            </tr>
        `;

        mostrarMensagem(erro.message, true);
    }
}

function obterNomeCliente(pedido) {
    if (pedido.clienteSnapshot) {
        return pedido.clienteSnapshot.nome
            || pedido.clienteSnapshot.nomeCliente
            || pedido.clienteId
            || "-";
    }

    return pedido.clienteId || "-";
}

function mostrarDetalhes(pedido) {
    const secao = document.getElementById("detalhes-pedido");

    if (!secao) {
        return;
    }

    document.getElementById("detalhe-id").textContent =
        pedido.id || "-";

    document.getElementById("detalhe-cliente").textContent =
        obterNomeCliente(pedido);

    document.getElementById("detalhe-status").textContent =
        pedido.status || "-";

    document.getElementById("detalhe-data").textContent =
        formatarData(pedido.criadoEm);

    document.getElementById("detalhe-total").textContent =
        formatarMoeda(pedido.total);

    const listaItens = document.getElementById("detalhe-itens");
    listaItens.innerHTML = "";

    if (pedido.itens && pedido.itens.length > 0) {
        pedido.itens.forEach((itemPedido) => {
            const item = document.createElement("li");

            item.textContent =
                `${itemPedido.nome || "Produto"} | ` +
                `SKU: ${itemPedido.sku || "-"} | ` +
                `Quantidade: ${itemPedido.quantidade} | ` +
                `Unitário: ${formatarMoeda(itemPedido.precoUnitario)} | ` +
                `Subtotal: ${formatarMoeda(itemPedido.subtotal)}`;

            listaItens.appendChild(item);
        });
    } else {
        listaItens.innerHTML = "<li>Nenhum item encontrado.</li>";
    }

    const endereco = pedido.enderecoEntrega;

    document.getElementById("detalhe-endereco").textContent =
        formatarEndereco(endereco);

    secao.hidden = false;
    secao.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

function formatarEndereco(endereco) {
    if (!endereco) {
        return "Endereço não informado.";
    }

    const partes = [
        endereco.logradouro,
        endereco.numero,
        endereco.complemento,
        endereco.bairro,
        endereco.cidade,
        endereco.uf,
        endereco.cep
    ];

    return partes
        .filter((parte) => parte !== null && parte !== undefined && parte !== "")
        .join(", ");
}

function formatarMoeda(valor) {
    if (valor === null || valor === undefined || valor === "") {
        valor = 0;
    }

    return Number(valor).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

function formatarData(data) {
    if (!data) {
        return "-";
    }

    const dataFormatada = new Date(data);

    if (Number.isNaN(dataFormatada.getTime())) {
        return data;
    }

    return dataFormatada.toLocaleString("pt-BR");
}

function mostrarMensagem(texto, erro) {
    const mensagem = document.getElementById("mensagem");

    if (!mensagem) {
        return;
    }

    mensagem.textContent = texto;
    mensagem.style.color = erro ? "red" : "";
}
