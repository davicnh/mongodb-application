const API_URL = "/produtos";

const formulario = document.getElementById("form-produto");
const listaProdutos = document.getElementById("lista-produtos");
const mensagem = document.getElementById("mensagem");
const botaoAtualizar = document.getElementById("btn-atualizar");

async function carregarProdutos() {
    listaProdutos.replaceChildren();

    const carregando = document.createElement("tr");
    const celula = document.createElement("td");
    celula.colSpan = 7;
    celula.textContent = "Carregando produtos...";
    carregando.appendChild(celula);
    listaProdutos.appendChild(carregando);

    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar os produtos.");
        }

        const produtos = await resposta.json();
        listaProdutos.replaceChildren();

        if (produtos.length === 0) {
            const linha = document.createElement("tr");
            const coluna = document.createElement("td");
            coluna.colSpan = 7;
            coluna.textContent = "Nenhum produto cadastrado.";
            linha.appendChild(coluna);
            listaProdutos.appendChild(linha);
            return;
        }

        produtos.forEach(produto => {
            const linha = document.createElement("tr");

            const valores = [
                produto.nome,
                produto.sku,
                produto.categoria,
                Number(produto.precoAtual).toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL"
                }),
                produto.estoque,
                produto.ativo ? "Ativo" : "Inativo"
            ];

            valores.forEach(valor => {
                const coluna = document.createElement("td");
                coluna.textContent = valor ?? "";
                linha.appendChild(coluna);
            });

            const colunaAcoes = document.createElement("td");

            const botaoEditar = document.createElement("button");
            botaoEditar.textContent = "Editar";
            botaoEditar.addEventListener("click", () => editarProduto(produto));

            const botaoExcluir = document.createElement("button");
            botaoExcluir.textContent = "Excluir";
            botaoExcluir.addEventListener("click", () => excluirProduto(produto));

            colunaAcoes.append(botaoEditar, botaoExcluir);
            linha.appendChild(colunaAcoes);

            listaProdutos.appendChild(linha);
        });

    } catch (erro) {
        listaProdutos.replaceChildren();

        const linha = document.createElement("tr");
        const coluna = document.createElement("td");
        coluna.colSpan = 7;
        coluna.textContent = erro.message;
        linha.appendChild(coluna);
        listaProdutos.appendChild(linha);
    }
}

formulario.addEventListener("submit", async evento => {
    evento.preventDefault();

    const produto = {
        nome: formulario.nome.value.trim(),
        sku: formulario.sku.value.trim(),
        categoria: formulario.categoria.value.trim(),
        descricao: formulario.descricao.value.trim(),
        precoAtual: Number(formulario.precoAtual.value),
        estoque: Number(formulario.estoque.value),
        ativo: formulario.ativo.checked
    };

    try {
        const resposta = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(produto)
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível cadastrar o produto.");
        }

        formulario.reset();
        mensagem.textContent = "Produto cadastrado com sucesso!";
        await carregarProdutos();

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
});

async function excluirProduto(produto) {
    if (!confirm(`Deseja excluir o produto "${produto.nome}"?`)) {
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/${produto.id}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível excluir o produto.");
        }

        mensagem.textContent = "Produto excluído com sucesso!";
        await carregarProdutos();

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
}

async function editarProduto(produto) {
    const nome = prompt("Nome do produto:", produto.nome);

    if (nome === null || nome.trim() === "") {
        return;
    }

    const preco = prompt("Preço:", produto.precoAtual);

    if (preco === null) {
        return;
    }

    const precoAtual = Number(preco.replace(",", "."));

    if (!Number.isFinite(precoAtual) || precoAtual <= 0) {
        mensagem.textContent = "Informe um preço válido.";
        return;
    }

    const atualizado = {
        ...produto,
        nome: nome.trim(),
        precoAtual
    };

    try {
        const resposta = await fetch(`${API_URL}/${produto.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(atualizado)
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível atualizar o produto.");
        }

        mensagem.textContent = "Produto atualizado com sucesso!";
        await carregarProdutos();

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
}

botaoAtualizar.addEventListener("click", carregarProdutos);

carregarProdutos();
